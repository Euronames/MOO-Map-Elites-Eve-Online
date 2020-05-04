/* ----------------------------------------------------------------- */
/* ----------------------------------------------------------------- */
/* Joins the attributeName from dgmattribs table */
SELECT DISTINCT attributes.attributeName
FROM dgmattribs as attributes
         INNER JOIN (
    /* Links values and attributeID, typeName and typeID for all modules */
    /* Joins the invtypes table with the dgmtypeattribs table */
    SELECT details.value, details.attributeID, moduleids.typeName, moduleids.typeID
    FROM dgmtypeattribs AS details
             INNER JOIN (
        /* Find all typeID and name of modules */
        SELECT typeID, typeName
        FROM invtypes
                 INNER JOIN (
            SELECT groupID
            FROM invgroups
            WHERE categoryID = (
                SELECT categoryID
                FROM invcategories
                WHERE categoryName = "Module"
            )
        ) AS groups
                            ON invtypes.groupID = groups.groupID
    ) AS moduleids
                        ON details.typeID = moduleids.typeID
) AS a
                    ON attributes.attributeID = a.attributeID AND attributes.attributeName LIKE "%velocity%"
/* ----------------------------------------------------------------- */
/* ----------------------------------------------------------------- */