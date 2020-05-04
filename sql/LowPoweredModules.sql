/* ----------------------------------------------------------------- */
/* ----------------------------------------------------------------- */

/* Joins the attribute name, value, type name and effect name for all possible modules*/
SELECT c.attributeName, c.attributeID, c.typeName, c.typeID, dgmeffects.effectName, c.effectID, c.value FROM dgmeffects INNER JOIN (
	/* Joins the effectID from junction table */
	SELECT b.attributeName, b.value, b.typeName, b.typeID, b.attributeID, dgmtypeeffects.effectID FROM dgmtypeeffects INNER JOIN (
		/* Joins the attributeName from dgmattribs table */
		SELECT attributes.attributeName, a.value, a.typeName, a.typeID, a.attributeID FROM dgmattribs as attributes INNER JOIN (
			/* Links values and attributeID, typeName and typeID for all modules */
			/* Joins the invtypes table with the dgmtypeattribs table */
			SELECT details.value, details.attributeID, moduleids.typeName, moduleids.typeID FROM dgmtypeattribs AS details INNER JOIN (
				/* Find all typeID and name of modules */
				SELECT typeID, typeName FROM invtypes INNER JOIN (
					SELECT groupID FROM invgroups WHERE categoryID = (
						SELECT categoryID FROM invcategories WHERE categoryName = "Module"
					)
				) AS groups
				ON invtypes.groupID = groups.groupID AND typeName = "Upgraded Armor EM Hardener I"
			) AS moduleids
			ON details.typeID = moduleids.typeID
		) AS a
		ON attributes.attributeID = a.attributeID
	) AS b
	ON dgmtypeeffects.typeID = b.typeID
) AS c
ON dgmeffects.effectID = c.effectID AND c.typeID IN (SELECT dgmtypeeffects.typeID FROM dgmtypeeffects WHERE effectID = 11)
/* ----------------------------------------------------------------- */
/* ----------------------------------------------------------------- */