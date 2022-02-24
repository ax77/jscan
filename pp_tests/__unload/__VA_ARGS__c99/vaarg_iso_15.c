#define debug(format, ...) fprintf (stderr, format, __VA_ARGS__, last)

debug("hello, no args");
debug("hello, one arg %s", "bye");
debug("hello, two args %d %s", 2, "bye");
