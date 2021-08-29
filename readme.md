# Useful functions for your project

### functions used in the examples

```
function print(&message)
	log(%&message%)
endfunction
```

```
function pow(#base,#exponent=2)
	#result = #base
	
	for(#i,2,%#exponent%)
		#result = #result * #base
	next
	
	return(%#result%)
endfunction
```

```
function greater10(#number)
	if(#number > 10)
		return(true)
	endif
	
	return(false)
endfunction
```

## common-data.txt examples

[array(...&values[])](common-data.txt#L1)
```
#array1[] = array(1,2,3,4,5)
// => [1, 2, 3, 4, 5]
&array2[] = array("One","Two","Three")
// => ["One", "Two", "Three"]
```

[range(#start,#stop=-1,#step=1)](common-data.txt#L6)
```
#range1[] = range(5)
// => [0, 1, 2, 3, 4, 5]
#range2[] = range(5,10)
// => [5, 6, 7, 8, 9, 10]
#range3[] = range(0,10,3)
// => [0, 3, 6, 9]
```

[fill(#amount,&filler)](common-data.txt#L21)
```
&fill1[] = fill(5)
// => ["", "", "", "", ""]
#fill2[] = fill(3,0)
// => [0, 0, 0]
```

## common-array.txt examples

[each(&array[],&function)](common-array.txt#L1)
```
#array[] = range(0,3)
each(#array[],print)
// Chat log:
// 0
// 1
// 2
// 3
```

[select(&array[],&function)](common-array.txt#L8)
```
#array[] = range(0,3)
#map[] = select(#array[],pow)
// => [0, 1, 4, 9]
```

[where(&array[],&function)](common-array.txt#L17)
```
#array[] = range(0,20,5)
#where[] = where(#array[],greater10)
// => [15, 20]
```

[chain(&array[],...&instructions[])](common-array.txt#L29)
```
#array[] = range(0,5)
chain(#array[],"select->pow","where->greater10","each->print")
// Chat log:
// 16
// 25
#chain[] = chain(#array[],"select->pow","select->pow")
// => [0, 1, 16, 81, 256, 625]
```

[first(&array[])](common-array.txt#L41)
```
&array[] = fill(3)
&array[] = "First"
&array[] = "Last"
&first = first(&array[])
// => "First"
```

[last(&array[])](common-array.txt#L50)
```
&array[] = fill(3,"Not last")
&array[] = "Last"
&last = last(&array[])
// => "Last"
```

[skip(&array[],#amount)](common-array.txt#L57)
```
#array = range(0,5)
#skip[] = skip(#array[],3)
// => [3, 4, 5]
```

[take(&array[],#amount)](common-array.txt#L68)
```
#array[] = range(0,5)
#take[] = take(#array[],3)
// => [0, 1, 2]
```