package com.ddoerr.modules.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	private final IMacro parentMacro;
	
	private final IScriptActionProvider provider;
	private final FunctionVariableProvider variableProvider = new FunctionVariableProvider();
	private final List<IVariableProvider> additionalVariableProviders = new ArrayList<>();

	private final Map<String, Object> stateData = new HashMap<>();
	
	private boolean isDead = false;
	private boolean isDirty = false;
	private boolean isSynchronous = false;
	
	public FunctionMacro(IMacro parentMacro, IScriptActionProvider provider) {
		this.parentMacro = parentMacro;
		this.provider = provider;
	}
	
	public IMacro getParentMacro() {
		return parentMacro;
	}
	
	@Override
	public void updateVariables(boolean clock) {
		parentMacro.updateVariables(clock);
	}

	@Override
	public Object getVariable(String variableName) {
		Object result = provider.getVariable(variableName, null);
		
		if (result != null) {
			return result;
		}
		
		for (IVariableProvider additionalProvider : additionalVariableProviders) {
			result = additionalProvider.getVariable(variableName);
			
			if (result != null) {
				return result;
			}
		}
		
		return variableProvider.getVariable(variableName);
	}

	@Override
	public Set<String> getVariables() {
		return Collections.emptySet();
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void setVariable(String variableName, boolean variableValue) {
		provider.setFlagVariable(this, variableName, variableValue);
	}

	@Override
	public void setVariable(String variableName, int variableValue) {
		provider.setVariable(this, variableName, variableValue);
	}

	@Override
	public void setVariable(String variableName, String variableValue) {
		provider.setVariable(this, variableName, variableValue);
	}

	@Override
	public void setVariables(Map<String, Object> variables) {
		for (Entry<String, Object> entry : variables.entrySet()) {
			if (entry.getValue() instanceof Boolean) {
				setVariable(entry.getKey(), (boolean)entry.getValue());
			} else if (entry.getValue() instanceof Integer) {
				setVariable(entry.getKey(), (int)entry.getValue());
			} else if (entry.getValue() instanceof String) {
				setVariable(entry.getKey(), (String)entry.getValue());
			}
		}
	}

	@Override
	public IMacroStatus getStatus() {
		return parentMacro.getStatus();
	}

	@Override
	public IMacroTemplate getTemplate() {
		return null;
	}

	@Override
	public boolean play(boolean trigger, boolean clock) throws ScriptException {
		return false;
	}

	@Override
	public void refreshPermissions() {
		parentMacro.refreshPermissions();
	}

	@Override
	public int getID() {
		return parentMacro.getID();
	}

	@Override
	public String getDisplayName() {
		return parentMacro.getDisplayName();
	}

	@Override
	public void setSynchronous(boolean synchronous) {
		isSynchronous = synchronous;
	}

	@Override
	public boolean isSynchronous() {
		return isSynchronous;
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
		return parentMacro.getContext();
	}

	@Override
	public Map<String, Object> getStateData() {
		return stateData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getState(String key) {
		return (T) stateData.get(key);
	}

	@Override
	public <T> void setState(String key, T value) {
		stateData.put(key, value);
	}

	@Override
	public void markDirty() {
		isDirty = true;
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public void kill() {
		isDead = true;
		parentMacro.kill();
	}
	
	public void killThis() {
		isDead = true;
	}

	@Override
	public boolean isDead() {
		return isDead;
	}

	@Override
	public void registerVariableProvider(IVariableProvider variableProvider) {
		additionalVariableProviders.add(variableProvider);
	}

	@Override
	public void unregisterVariableProvider(IVariableProvider variableProvider) {
		additionalVariableProviders.remove(variableProvider);
	}

}
