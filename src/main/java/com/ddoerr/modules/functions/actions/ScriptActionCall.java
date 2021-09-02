package com.ddoerr.modules.functions.actions;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import com.ddoerr.modules.functions.ActionExecutable;
import com.ddoerr.modules.functions.CachedScriptParser;
import com.ddoerr.modules.functions.Executable;
import com.ddoerr.modules.functions.FunctionExecutable;
import com.ddoerr.modules.functions.FunctionMacro;
import com.ddoerr.modules.functions.ModuleInfo;
import com.ddoerr.modules.functions.VariableHandler;
import com.ddoerr.modules.functions.actions.FunctionState.Argument;
import com.ddoerr.modules.functions.parser.ActionParserCall;

import net.eq2online.macros.core.executive.MacroAction;
import net.eq2online.macros.core.executive.MacroActionProcessor;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptAction;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.client.Minecraft;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionCall extends ScriptAction {
	class State {
		private final IMacroActionProcessor actionProcessor;
		private final IMacro macro;
		
		public State(IMacroActionProcessor actionProcessor, IMacro macro) {
			this.actionProcessor = actionProcessor;
			this.macro = macro;
		}
		
		public IMacroActionProcessor getActionProcessor() {
			return actionProcessor;
		}
		
		public IMacro getMacro() {
			return macro;
		}
	}
	
	public ScriptActionCall() {
		super(ScriptContext.MAIN, "call");
	}
	

	public ScriptActionCall(String name) {
		super(ScriptContext.MAIN, name);
	}
	
	@Override
	public void onInit() {
		this.context.getCore().registerScriptAction(this);
		this.context.getCore().getParser().addActionParser(new ActionParserCall(context));
	}
	
	@Override
	public boolean canExecuteNow(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams,
			String[] params) {
		System.out.println(Minecraft.getMinecraft().world.getTotalWorldTime());
		Executable executable = instance.getState();
		
		if (executable == null) {
			String functionName = params.length == 0 ? "default" : provider.expand(macro, params[0], false);
			
			IMacro parent = macro;
			FunctionState functionState = macro.getState("fn#" + functionName.toLowerCase());
			
			while (functionState == null && parent instanceof FunctionMacro) {
				parent = ((FunctionMacro)parent).getParentMacro();
				functionState = parent.getState("fn#" + functionName.toLowerCase());
			}
			
			if (functionState != null) {			
				IMacroActionProcessor actionProcessor = MacroActionProcessor.compile(new CachedScriptParser(functionState.getActions()), "$${}$$", 100, 100, macros);
				
				if (instance.getActionProcessor().isUnsafe()) {
					int maxActionsPerTick = 100;
					try {
						Field maxActionsPerTickField = actionProcessor.getClass().getField("maxActionsPerTick");
						maxActionsPerTickField.setAccessible(true);
						maxActionsPerTick = maxActionsPerTickField.getInt(actionProcessor);
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
					actionProcessor.beginUnsafeBlock(null, null, null, maxActionsPerTick);
				}
				
				IMacro functionMacro = new FunctionMacro(macro, provider);
				
				List<Argument> arguments = functionState.getArguments();
				
				for (int i = 0; i < arguments.size(); i++) {					
					boolean hasRemainingValues = i + 1 < params.length;
					
					Argument argument = arguments.get(i);
						
					try {
						if (!hasRemainingValues && !argument.hasDefaultValue()) {
							break;
						}
						
						if (!argument.isValid()) {
							continue;
						} else if (argument.isCatchAll()) {
							List<String> values = Arrays.stream(params)
								.skip(i + 1)
								.map((value) -> VariableHandler.expand(macro, value))
								.collect(Collectors.toList());
							
							VariableHandler.setArray(functionMacro, argument.getName(), values);
						} else if (argument.isArray()) {
							if (hasRemainingValues) {
								VariableHandler.copyArray(macro, params[i + 1], functionMacro, argument.getName());
							} else {
								String[] defaultValues = (String[])argument.getDefaultValue();							
								VariableHandler.setArray(functionMacro, argument.getName(), defaultValues);
							}
						} else {
							String argumentValue = hasRemainingValues ? params[i + 1] : (String)argument.getDefaultValue();						
							String expandedValue = VariableHandler.expand(macro, argumentValue);
							VariableHandler.setScalar(functionMacro, argument.getName(), expandedValue);
						}
					} catch (Exception e) {
						provider.actionAddChatMessage("Exception happened while trying to handle the " + (i + 1) + ". argument (" + argument.getName() + ") of the function " + functionName);
						
						System.out.println(String.join(",", params));
						e.printStackTrace();
					}
				}
				
				instance.setState(executable = new FunctionExecutable(actionProcessor, functionMacro));
			} else {
				IScriptAction scriptAction = macro.getContext().getScriptContext().getAction(functionName);
				
				if (scriptAction == null) {
					provider.actionAddChatMessage("Could not find function or action " + functionName);
					return true;
				}
				
				IMacroAction macroAction = new MacroAction(
						instance.getActionProcessor(),
						scriptAction,
						rawParams.substring(params[0].length() + 1),
						((MacroAction)instance).getUnparsedParams().substring(params[0].length() + 1),
						ArrayUtils.subarray(params, 1, params.length),
						instance.hasOutVar() ? instance.getOutVarName() : null);
				
				instance.setState(executable = new ActionExecutable(macroAction, macro));
			}
		}
		
		return executable.canExecute();
	}
	
	@Override
	public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams,
			String[] params) {
		Executable executable = instance.getState();
		
		if (executable == null) {
			return VariableHandler.getEmpty();
		}
		
		IReturnValue returnValue = executable.execute();
		
		if (returnValue == null) {
			// Makes sure that you can always assign the result of a function call to a variable
			// Otherwise it would throw an exception
			returnValue = VariableHandler.getEmpty();
		}
		
		macro.setState("chain_value", returnValue);
		macro.setState("chain_variable", instance.getOutVarName());
		return returnValue;
	}
}
