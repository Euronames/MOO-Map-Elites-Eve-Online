
import jnius_config

from hypothesis import given
import hypothesis.strategies as st

jnius_config.set_classpath('.', '/Users/xrosby/Desktop/Git/MOO-Map-Elites-Eve-Online_FUNK/src/')

from jnius import autoclass

Ship = autoclass('EveOnline.Ship.Ship')






@given(\
    numberOfRigs = st.integers(max_value=10000, min_value=0)\
    , numberOfHighModules = st.integers(max_value=10000, min_value=0)\
    , numberOfMediumModules = st.integers(max_value=10000, min_value=0)\
    , numberOfLowModules = st.integers(max_value=10000, min_value=0)\
    , cpuTotal = st.floats(max_value=10000, min_value=0)\
    , powerTotal = st.floats(max_value=10000, min_value=0)\
    , calibrationTotal = st.floats(max_value=10000, min_value=0))
def generate_ship(numberOfRigs, numberOfHighModules, numberOfMediumModules, numberOfLowModules, cpuTotal, powerTotal, calibrationTotal):
    new_ship = Ship(numberOfRigs, numberOfHighModules, numberOfMediumModules, numberOfLowModules, cpuTotal, powerTotal, calibrationTotal)
    print(new_ship.toString())
    assert(new_ship != None)


generate_ship()