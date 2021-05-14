package com.ddoerr.modules.functions.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IMacroActionProcessor;
import net.eq2online.macros.scripting.parser.ActionParserAbstract;
import net.eq2online.macros.scripting.parser.ScriptContext;

public class ActionParserFunction extends ActionParserAbstract {
	private static Pattern functionPattern = Pattern.compile("^function ([a-z0-9\\_]+)\\((.*?)\\)$", Pattern.CASE_INSENSITIVE);

	public ActionParserFunction(ScriptContext context) {
		super(context);
	}
	
	@Override
	public IMacroAction parse(IMacroActionProcessor actionProcessor, String scriptEntry) {
		Matcher matcher = functionPattern.matcher(scriptEntry);
		
		if (matcher.matches()) {
			String name = matcher.group(1);
			String arguments = matcher.group(2);
			
			return parse(actionProcessor, "function", name + "," + arguments, null);
		}
		
		return null;
	}

}
