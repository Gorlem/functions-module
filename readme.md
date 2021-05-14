# Functions module

## Usage

Function definition

```
function <name>([variable, ...]);endfunction
```


Return a value inside of an function

```
return <value>
```


Call a defined function

```
<name>([value, ...])
```


Alternative way of calling functions

```
CALL(<name>,[value, ...])
```


## Example

```
function greet(&name)
	&message = "Hello %&name%!"
	log(%&message%)
	return %&message%
endfunction

greet("you")
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
