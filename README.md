# hmpps-reference-data

🧑‍🔬 This is an experimental project: it may change.

## Intent

Store reference data as flat files with the following characteristics:

- each entry has a unique ID
- the unique ID is in the _first_ column
- entries are not deleted (instead, add a `start_date`/`end_date` column pair)
- data structure changes are backwards-compatible in the same file:
    - cannot remove existing columns
    - cannot rename existing columns
    - can add new columns

## Checking backwards compatibility

To build, test, assemble the project:
```
$ ./gradlew build
<snip>
BUILD SUCCESSFUL in 2s
10 actionable tasks: 10 executed
```

To run the tool:
```
$ ./gradlew run

> Task :app:run
🔍 Checking if register files are backwards compatible with 'origin/main'...
registers/nomis-ethnicity.csv: ✅ pass
registers/nomis-gender.csv: ✅ pass
registers/nomis-locations.csv: ✅ pass
registers/nomis-suffix.csv: ✅ pass
registers/nomis-titles.csv: ✅ pass
registers/offences.csv: ✅ pass
registers/pcc-regions-for-probation-v0.csv: ✅ pass
registers/probation-delivery-units-v0.csv: ✅ pass
registers/probation-offices-v0.csv: ✅ pass
registers/probation-regions-v0.csv: ✅ pass

BUILD SUCCESSFUL in 1s
2 actionable tasks: 1 executed, 1 up-to-date
```
