Author : David Hay

This merge code is used to retrospectively populate registrations in the eac database with eRightsLicenceIds.

This merge code is not part of any eac application. It is intended to be run once as part of the upgrade to R2.

This merge code is intended to be run from a developer's pc with connections to an eac database and the associated eRights Web Service.

The database connection is configured in src/test/resources/eac/merge.dataSource.xml
The atypon web service connection is configured in src/test/java/com/oup/eac/service/merge/BaseMergeTest

 


