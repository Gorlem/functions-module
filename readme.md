# Functions module

## Usage

Function definition

```
function <name>([variable, ...]);endfunction
```


Return a value inside of an function

```
return(<value>)
```


Call a defined function

```
<name>([value, ...])
```


Alternative way of calling functions (`<name>` can contain variables)

```
CALL(<name>,[value, ...])
```


`<value>` can either be an array specifier (`&array[]`) or a normal string (`"Hello World"`, `%TIME%`), which gets expanded.

## Examples

```
function greet(&name)
	&message = "Hello %&name%!"
	log(%&message%)
	return(%&message%)
endfunction

greet("you")
```

```
function logarray(&array[])
	foreach(#arr[],&content,#index)
		log(%#index% => %&content%)
	next
endfunction

unset(&test[])
&test[] = "Hello"
&test[] = "World"

logarray(&test[])
```

```
function fill(#amount,&filler)
	for(#i,1,%#amount%)
		&array[] = %&filler%
	next
	return(&array[])
endfunction

&filled[] = fill(5,"---")
```

```
function data()
	return("Alex","Sam","Kim")
endfunction

&names[] = data()
```

## Notes

Actions defined by the mod or other modules will always have the priority over functions.
That means that if you e.g. define a function named log, you cannot call the function as an action.
For those cases you can still call the functions with the `CALL` action.

## Changelog

### v0.1

 * Initial Version

### v0.2

 * Fixed arguments not getting expanded. (Issue #4)
 * Fixed passed arguments not getting parsed according to their type. (Issue #5)
 * Function names are now case-insensitive. (Issue #1)

### v0.3
 * Added `functions` iterator, which has the `FUNCTIONNAME` variable. (Issue #3)
 * Function names can now contain numbers.
 * Fixed a bug with functions which don't contain arguments.

### v0.4
 * Function arguments and return values can now be arrays. (Issue #6)
 * The `RETURN` syntax without brackets will be removed in v1.0, please use it like a normal action with brackets.
 * Functions can be called dynamically based on variables with the `CALL` action. (Issue #8)
 * `RETURN` also now allows multiple parameters, which will each get expanded and turned into an array.
