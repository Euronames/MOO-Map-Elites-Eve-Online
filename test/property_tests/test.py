
import jnius_config

from hypothesis import given
import hypothesis.strategies as st

jnius_config.set_classpath('.', './../MOO-Map-Elites-Eve-Online_FUNK/src/')

from jnius import autoclass

Ship = autoclass('EveOnline.Ship.Ship')
MapElitesInterface = autoclass('EveOnline.ShipBuilder')

@st.composite
def ship_generator(draw):
    numberOfRigs = draw(st.integers(max_value=10000, min_value=0))
    numberOfHighModules = draw(st.integers(max_value=10000, min_value=0))
    numberOfMediumModules = draw(st.integers(max_value=10000, min_value=0))
    numberOfLowModules = draw(st.integers(max_value=10000, min_value=0))
    cpuTotal = draw(st.floats(max_value=10000, min_value=0))
    powerTotal = draw(st.floats(max_value=10000, min_value=0))
    calibrationTotal = draw(st.floats(max_value=10000, min_value=0))
    ship_name = draw(st.text(min_size=10, max_size=100))
    new_ship = Ship(\
        numberOfRigs\
        , numberOfHighModules\
        , numberOfMediumModules\
        , numberOfLowModules\
        , cpuTotal\
        , powerTotal\
        , calibrationTotal)
    new_ship.addTypeName(ship_name)
    print(new_ship.toString())
    return new_ship

@st.composite
def ship_list_generator(draw, elements=ship_generator()):
    ship_list = draw(st.lists(elements, min_size=2, max_size=1000))
    return ship_list
"""
@given(ships=ship_list_generator())
def test_ships(ships):
    assert(len(ships) > 1)
"""
@given(ship=ship_generator())
def test_random_mutation(ship):
    mei = MapElitesInterface()
    mutation = mei.randomMutation(ship)
    assert(mutation != ship)

    
#test_ships()

test_random_mutation()

@given
def test_feature_space(ship):
    print("TESTING FEATURESPACE")