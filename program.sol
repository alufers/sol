print "Hello world!";
print "";
print "Witamy w pierwszym na swiecie programie napisanym w SOLu";

print "2 + 2 = " + (2 + 2);
print "TE" + "ST";
print "Test modulo: ";
print "20 % 3 = " + (20 % 3);
print "Test pow: ";
print "2 ** 3 = " + (2 ** 3);
print "Test arithmetic: ";
print "2+2-4*6/7%2**3 = " + (2+2-4*6/7%2**3);


mut x = 10;
mut y = 6 * 2;
mut z = x * y;

print "z = " + z;

mut mutated = 9*3;
mutated = "str";
print "mutated = " + mutated;

mut shadowed = 5;
print "original shadowed = " + shadowed;
{
    mut shadowed = 10;
    print "shadowed = " + shadowed;
}

// test or
print "TRUTHY + OR OK" or 10;
print nil or "NIL + OR OK";

print 5 == 5;

if(true) {
    print "IF OK";
}

if(10 < 5) {

} else {
    print "ELSE OK";
}

mut i = 0;
while (i < 100) {

    print "i = " + i;
    i = i + 1;
}

while(true) {
    break;
}

while(true) {
    if(2 >= -1) {
        break;
    }
}
for mut i = 0; i < 10; i = i + 1 {
    print i;
}


fun dupa {
    print "FUNCTION OK";
}

dupa();

fun dupaArg arg {
    print "ARG OK: " + arg;
}

dupaArg(3);

fun adder toAdd {
    fun inner n return toAdd + n;
    return inner;
}

print "CLOSURES OK: " + adder(5)(10);

