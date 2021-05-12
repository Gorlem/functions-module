package com.ddoerr.modules.functions.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.parser.ActionParserAbstract;
import net.eq2online.macros.scripting.parser.ScriptContext;

public class ActionParserReturn extends ActionParserAbstract {
	private static Pattern returnPattern = Pattern.compile("^return (.+?)$", Pattern.CASE_INSENSITIVE);
	
	public ActionParserReturn(ScriptContext context) {
		super(context);
	}

	@Override
	public IMacroAction parse(IMacroActionProcessor actionProcessor, String scriptEntry) {
		Matcher matcher = returnPattern.matcher(scriptEntry);
		
		if (matcher.matches()) {
			String parameters = matcher.group(1);
			
			return parse(actionProcessor, "return", parameters, null);
		}
		
		
		return null;
	}

}
