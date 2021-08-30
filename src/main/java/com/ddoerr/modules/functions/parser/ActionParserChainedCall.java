package com.ddoerr.modules.functions.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.parser.ActionParserAbstract;
import net.eq2online.macros.scripting.parser.ScriptContext;

public class ActionParserChainedCall extends ActionParserAbstract {
	private static Pattern chainPattern = Pattern.compile("^-> ([a-z1-9\\_]+)\\((.*?)\\)$", Pattern.CASE_INSENSITIVE);
	
	public ActionParserChainedCall(ScriptContext context) {
		super(context);
	}
	
	@Override
	public IMacroAction parse(IMacroActionProcessor actionProcessor, String scriptEntry) {
		Matcher matcher = chainPattern.matcher(scriptEntry);
        
		if (matcher.matches())
        {
            String name = matcher.group(1);
            String parameters = matcher.group(2);
            
            return this.parse(actionProcessor, "chainedcall", name + "," + parameters, null);
        }
        
		return null;
	}

}
