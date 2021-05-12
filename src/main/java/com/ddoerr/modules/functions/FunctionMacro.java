package com.ddoerr.modules.functions;

import java.util.Collections;
import java.util.HashMap;
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
import net.eq2online.macros.scripting.variable.VariableCache;

public class FunctionMacro implements IMacro {
	static class Cache extends VariableCache {
		@Override
		public Object getVariable(String variableName) {
			return this.getCachedValue(variableName);
		}
	}
	
	private final IMacro parentMacro;
	
	private final IScriptActionProvider provider;
	private final VariableCache variableCache = new Cache();
	private final FunctionVariableProvider variableProvider = new FunctionVariableProvider();

	private final Map<String, Object> stateData = new HashMap<>();
	
	private boolean isDead = false;
	
	public FunctionMacro(IMacro parentMacro, IScriptActionProvider provider) {
		this.parentMacro = parentMacro;
		this.provider = provider;
	}
	
	public IMacro getParentMacro() {
		return parentMacro;
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
		
		result = variableCache.getVariable(variableName);
		
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
		variableCache.storeVariable(variableName, variableValue);
	}

	@Override
	public void setVariable(String variableName, int variableValue) {
		variableCache.storeVariable(variableName, variableValue);
	}

	@Override
	public void setVariable(String variableName, String variableValue) {
		variableCache.storeVariable(variableName, variableValue);
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
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void kill() {
		isDead = true;
	}

	@Override
	public boolean isDead() {
		return isDead;
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
