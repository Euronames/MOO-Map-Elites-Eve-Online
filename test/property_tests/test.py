from hypothesis import given, settings,HealthCheck
import hypothesis.strategies as st

import jnius_config

jnius_config.set_classpath('.', '/Users/xrosby/Desktop/Git/MOO-Map-Elites-Eve-Online_FUNK/out/production/bachelor-2019---naming-may-change:/Users/xrosby/Desktop/Git/MOO-Map-Elites-Eve-Online_FUNK/lib/sqlite-jdbc-3.23.1.jar:/Users/xrosby/Desktop/Git/MOO-Map-Elites-Eve-Online_FUNK/lib/gson-2.8.5.jar')

from jnius import autoclass

Ship = autoclass('EveOnline.Ship.Ship')
ShipBuilder = autoclass('EveOnline.ShipBuilder')

@st.composite
def ship_generator(draw):
    numberOfRigs = draw(st.integers(max_value=10000, min_value=1))
    numberOfHighModules = draw(st.integers(max_value=10000, min_value=1))
    numberOfMediumModules = draw(st.integers(max_value=10000, min_value=1))
    numberOfLowModules = draw(st.integers(max_value=10000, min_value=1))
    cpuTotal = draw(st.floats(max_value=10000, min_value=1))
    powerTotal = draw(st.floats(max_value=10000, min_value=1))
    calibrationTotal = draw(st.floats(max_value=10000, min_value=1))
    ship_name = draw(st.text(min_size=10, max_size=20))
    new_ship = Ship(\
        numberOfRigs\
        , numberOfHighModules\
        , numberOfMediumModules\
        , numberOfLowModules\
        , cpuTotal\
        , powerTotal\
        , calibrationTotal)
    return new_ship

@st.composite
def generate_stabber(draw):
    ship = draw(ship_generator())
    ship.addTypeName("Stabber")
    return ship


@st.composite
def ship_generator_with_components(draw):
    ship_builder = ShipBuilder()
    ship = draw(generate_stabber())
    print(ship.toString())
    ship_builder.generateRandomSolution(ship)
    return ship

@settings(suppress_health_check=(HealthCheck.too_slow,))
@given(ship_with_components = ship_generator_with_components())
def test_ship_with_components(ship_with_components):
    assert(ship_with_components != None)

@given(ship=ship_generator())
def test_random_mutation_no_components(ship):
    ship_builder = ShipBuilder()
    mutation = ship_builder.randomMutation(ship)
    assert(mutation != ship)


test_ship_with_components()

#test_random_mutation_no_components()
 






"""
@st.composite
def ship_list_generator(draw, elements=ship_generator()):
    ship_list = draw(st.lists(elements, min_size=2, max_size=1000))
    return ship_list


"""
