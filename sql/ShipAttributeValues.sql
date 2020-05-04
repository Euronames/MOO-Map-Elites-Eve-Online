/* ----------------------------------------------------------------- */
/* ----------------------------------------------------------------- */
/* Finds values of ship attributes */
/* Needed variables: value, attributeName */
SELECT attributes.attributeName, details.value FROM  (
	/* Selects all dgmattribs for ship */
	SELECT * FROM dgmattribs WHERE attributeID IN (
		SELECT attributeID FROM dgmtypeattribs WHERE typeID = 582
	)
) AS attributes
/* Join dgmattribs with values */
INNER JOIN (
	/* Selects values for modules */
	SELECT value, attributeID FROM dgmtypeattribs WHERE typeID = 582
)	AS details
/* Join value from dgmtypeattribs with attributeName of dgmattribs */
 ON attributes.attributeID = details.attributeID
/* ----------------------------------------------------------------- */
/* ----------------------------------------------------------------- */