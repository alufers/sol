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


var x = 10;
var y = 6 * 2;
var z = x * y;

print "z = " + z;

var mutated = 9*3;
mutated = "str";
print "mutated = " + mutated;

var shadowed = 5;
print "original shadowed = " + shadowed;
{
    var shadowed = 10;
    print "shadowed = " + shadowed;
}

// test or
print "test" or 10;
print nil or "nope";

print 5 == 5;

if(true) {
    print "IF OK";
}

if(10 < 5) {

} else {
    print "ELSE OK";
}