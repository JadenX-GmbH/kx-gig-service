INSERT INTO candidate_specialist (id,
                                  uid,
                                  date_created,
                                  last_updated)
VALUES (1700,
        'bf14fa45-aa3c-4fa6-8f30-80a71fa6af5c',
        '2020-09-03 04:30:00',
        '2020-09-03 04:30:00');
INSERT INTO gig_chosen_specialist (gig_id,
                                   candidate_specialist_id)
VALUES (1000, 1700);

INSERT INTO candidate_specialist (id,
                                  uid,
                                  date_created,
                                  last_updated)
VALUES (1701,
        'cf14fa45-aa3c-4fa6-8f30-80a71fa6af5a',
        '2020-09-03 04:30:00',
        '2020-09-03 04:30:00');
INSERT INTO gig_chosen_specialist (gig_id,
                                   candidate_specialist_id)
VALUES (1001, 1701);
