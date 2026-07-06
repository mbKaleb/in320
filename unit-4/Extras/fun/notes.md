Fix: in `minidump/streams/SystemInfoStream.py`, changed:

```
AARCH64 = 0x8003 #ARM64
```

to:

```
AARCH64 = 12 #ARM64
```

A one-line correction to an incorrect hardcoded constant, derived from the console output in `minidump-output.png`.
