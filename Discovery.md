# Skill Hunter Functionality Discovery

This document is a list of all future facing ideas that are not robust enough to go on the Roadmap.

## General
* Add Flyway for migrations??

## ContactMethod
* Only one `isPreferred` per `ContactMethodType`.

## Placement

* Consider `CompensationHistory` to track initial compensation, raises, promotions, contract rate changes, and effective dates.

## Position

* Compensation should probably include amount + period rather than assuming annual salary.
* Should it have `minExpectedDaysInOfficePerPeriod`, `maxExpectedDaysInOfficePerPeriod`, and `HybridPeriod` where `HybridPeriod` is WEEKLY, MONTHLY, QUARTERLY, ANNUALLY?
* `WorkType` needs to include INTERN, CONTRACT, PART_TIME, and maybe others

## Application

* `ApplicationStatus` may need an `isApplicationActive` flag.
* Workflow validation
  * Decide which statuses can go to what other statuses
    * ApplicationStatus may need to be more than an enum?
  * When a `PositionApplication` is marked as "accepted", create a `Placement` for that `Position`. Unless known, default to first business day after acceptance is marked.

