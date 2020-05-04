
SELECT invtypes.typeID, invtypes.typeName FROM invtypes WHERE invtypes.groupID IN (
SELECT invgroups.groupID FROM invgroups WHERE categoryID = 6);