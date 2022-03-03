javac -d bin/ src/cycling/*.java && jar cvf cycling.jar -C bin . && jar uvf cycling.jar -C src . && jar uvf cycling.jar doc && jar uvf cycling.jar res
