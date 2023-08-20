CREATE TABLE CustomerRatings
(
    Id           INT AUTO_INCREMENT PRIMARY KEY,
    CustomerNo   CHAR(10),
    RatingScore  VARCHAR(5),
    AnalysisType SMALLINT
);