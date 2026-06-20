# Skill Hunter Functionality Discovery

This document is a list of all future facing ideas that are not robust enough to go on the Roadmap.

## ContactInfo
* Only one `isPreferred` per `ContactInfoType`.

## Position

* Should it have `minExpectedDaysInOfficePerPeriod`, `maxExpectedDaysInOfficePerPeriod`, and `HybridPeriod` where `HybridPeriod` is WEEKLY, MONTHLY, QUARTERLY, ANNUALLY?

## Application

* `ApplicationStatus` may need an `isApplicationActive` flag.
* Workflow validation
  * Decide which statuses can go to what other statuses
    * ApplicationStatus may need to be more than an enum?
  * When a `PositionApplication` is marked as "accepted", create a `Placement` for that `Position`. Unless known, default to first business day after acceptance is marked.

