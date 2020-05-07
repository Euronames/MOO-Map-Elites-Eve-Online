from hypothesis import given, settings,HealthCheck
import hypothesis.strategies as st

import jnius_config

jnius_config.set_classpath('.', '/Users/xrosby/Desktop/Git/MOO-Map-Elites-Eve-Online_FUNK/out/production/MOO-Map-Elites-Eve-Online:/Users/xrosby/Desktop/Git/MOO-Map-Elites-Eve-Online_FUNK/lib/sqlite-jdbc-3.23.1.jar:/Users/xrosby/Desktop/Git/MOO-Map-Elites-Eve-Online_FUNK/lib/gson-2.8.5.jar')

from jnius import autoclass

Ship = autoclass('EveOnline.Ship.Ship')
ShipBuilder = autoclass('EveOnline.ShipBuilder')



#_______________ SHIP GENERATORS ____________________

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
    new_ship.addTypeName(ship_name)
    return new_ship

@st.composite
def generate_stabber(draw):
    Connection = autoclass('Database.Connection.SQLiteJDBCDriverConnection')
    ShipFetcher = autoclass('EveOnline.DataFetcher.ShipFetcher')
    ship_fetcher = ShipFetcher(Connection())
    print(ship_fetcher)
    ship = ship_fetcher.getShipByTypeID(622)
    print(ship)
    return ship



@st.composite
def ship_generator_with_components(draw):
    ship_builder = ShipBuilder()
    ship = draw(ship_generator())
    ship.addLowPoweredComponent(ship_builder.getRandomLowPoweredModule())
    print(ship.toString())
    return ship


@settings(suppress_health_check=(HealthCheck.too_slow,))
@given(ship_with_components = ship_generator_with_components())
def test_ship_with_components(ship_with_components):
    assert(ship_with_components != None)




#____________COMPONENT GENERATOR _______________

@st.composite
def component_names(draw):
    ComponentName = autoclass('EveOnline.Component.ComponentName')
    component_names = [ComponentName.Rig, ComponentName.LowPoweredModule, ComponentName.MediumPoweredModule, ComponentName.HighPoweredModule]
    random_component_index = draw(st.one_of(st.integers(min_value=0, max_value=len(component_names) - 1)))
    return component_names[random_component_index].toString()
    
@st.composite
def qualities(draw):
    Quality = autoclass('EveOnline.Component.Quality')
    quality_name = draw(st.text())
    quality_value = draw(st.floats())
    quality = Quality(quality_name, quality_value)
    print(quality.toString())
    return quality



@st.composite
def components(draw):
    Component = autoclass('EveOnline.Component.Component')
    ArrayList = autoclass('java.util.ArrayList')
    component = Component()
    quality_list = draw(st.lists(qualities()))
    array_list_qualities = ArrayList()
    array_list_qualities.add(quality_list)
    component_name = draw(component_names())
    component.addQualities(array_list_qualities)
    component.addTypeName(component_name)
    return component

    


#________________TESTS________________

@given(component = components())
def test_components(component):
    print(component.getComponentName())
    assert(component != None) 





@given(ship=ship_generator())
def test_random_mutation_no_components(ship):
    ship_builder = ShipBuilder()
    mutation = ship_builder.randomMutation(ship)
    assert(mutation != ship)

test_components()

#test_component_names()

#test_ship_with_components()

#test_random_mutation_no_components()
 






"""
@st.composite
def ship_list_generator(draw, elements=ship_generator()):
    ship_list = draw(st.lists(elements, min_size=2, max_size=1000))
    return ship_list


"""
