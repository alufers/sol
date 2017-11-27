fun ArrElem (val, prev) {
    fun inner cmd {
        if (cmd == "val") return val;
        if (cmd == "prev") return prev;
    }

    return inner;
}

fun printArr a {
    if(a("prev")) {
        printArr(a("prev"));
    }
    print a("val");

}

fun sumArr a {
    if(a("prev")) {
        return sumArr(a("prev")) + a("val");
    }
    print a("val");
}

fun fib n {
    if n == 0 or n == 1 return n;
    return fib(n - 1) + fib(n - 2);
}

mut fibs = ArrElem(0, nil);

for mut i = 1; i < 10; i = i + 1 {
    fibs = ArrElem(fib(i), fibs);
}

printArr(fibs);
print "SUM: " + sumArr(fibs);