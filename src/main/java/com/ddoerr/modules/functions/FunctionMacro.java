package com.ddoerr.modules.functions;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import net.eq2online.macros.scripting.api.ICounterProvider;
import net.eq2online.macros.scripting.api.IFlagProvider;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroActionContext;
import net.eq2online.macros.scripting.api.IMacroTemplate;
import net.eq2online.macros.scripting.api.IMutableArrayProvider;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IStringProvider;
import net.eq2online.macros.scripting.api.IVariableProvider;
import net.eq2online.macros.scripting.exceptions.ScriptException;

public class FunctionMacro implements IMacro {
	private final FunctionVariableProvider variableProvider = new FunctionVariableProvider();
	private final IScriptActionProvider provider;
	
	public FunctionMacro(IScriptActionProvider provider) {
		this.provider = provider;
	}
	
	
	@Override
	public void updateVariables(boolean clock) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getVariable(String variableName) {
		Object result = provider.getVariable(variableName, null);
		
		if (result != null) {
			return result;
		}
		
		return variableProvider.getVariable(variableName);
	}

	@Override
	public Set<String> getVariables() {
		return Collections.emptySet();
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVariable(String variableName, boolean variableValue) {
		
	}

	@Override
	public void setVariable(String variableName, int variableValue) {
		
	}

	@Override
	public void setVariable(String variableName, String variableValue) {
		
	}

	@Override
	public void setVariables(Map<String, Object> variables) {
		
	}

	@Override
	public IMacroStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMacroTemplate getTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean play(boolean trigger, boolean clock) throws ScriptException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshPermissions() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSynchronous(boolean synchronous) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSynchronous() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IFlagProvider getFlagProvider() {
		return variableProvider;
	}

	@Override
	public ICounterProvider getCounterProvider() {
		return variableProvider;
	}

	@Override
	public IStringProvider getStringProvider() {
		return variableProvider;
	}

	@Override
	public IMutableArrayProvider getArrayProvider() {
		return variableProvider;
	}

	@Override
	public IMacroActionContext getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getStateData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getState(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void setState(String key, T value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void markDirty() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerVariableProvider(IVariableProvider variableProvider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterVariableProvider(IVariableProvider variableProvider) {
		// TODO Auto-generated method stub

	}

}
