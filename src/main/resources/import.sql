-- ========================================================================
-- Project       : Master Thesis - Portlets based time management tools
-- Document      : import.sql
-- Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
-- Organization: : FIT VUT <http://www.fit.vutbr.cz>
-- ========================================================================

-- HINT: 
-- =====
-- You can find your portlet instance id in portlet HELP mode and replace IDs in this file. 
-- Ctrf + F => "Find and replace"

-- Used portlet instances ID:
-- a) Calendar
--    41a3c58a-a9d5-4799-8cf2-08a023ec14ec [portlet 1]
--    98f5cfff-7b82-4a12-9f74-01120a79dc8f [portlet 2]
-- b) Contacts
--    d2d2b315-1554-412a-bb98-b18224c12c1a [portlet 3]
--    9ed99a7f-cf30-4428-9717-6d8be5ee1de7 [portlet 4]
-- c) Tasks
--    14810e9c-af4d-4b05-b59e-06526c3fba56 [portlet 5]
--    2611c6dc-cef4-4112-8433-417f99109cd3 [portlet 6]


------------------------------------------------------------------
-- CALENDAR PORTLET
------------------------------------------------------------------

-- CALENDAR
-- [portlet 1]
insert into Calendar (dtype, calendar_id, private_id, portlet_instance_id, name, description, url, username, password) values ('Calendar', -1, '2d7428a6-b58c-4008-8575-f05549f16311', '41a3c58a-a9d5-4799-8cf2-08a023ec14ec', 'Private', 'Private calendar', null, null, null)
insert into Calendar (dtype, calendar_id, private_id, portlet_instance_id, name, description, url, username, password) values ('Calendar', -2, '2d7428a6-b58c-4008-8575-f05549f16312', '41a3c58a-a9d5-4799-8cf2-08a023ec14ec', 'Job', 'Calendar of my job', null, null, null)
insert into Calendar (dtype, calendar_id, private_id, portlet_instance_id, name, description, url, username, password) values ('ICalRemoteCalendar', -3, '2d7428a6-b58c-4008-8575-f05549f16313', '41a3c58a-a9d5-4799-8cf2-08a023ec14ec', 'Remote iCal', 'Remote Calendar description', 'https://www.google.com/calendar/ical/39gpmbr1c89fv54gdrrd46ktm0%40group.calendar.google.com/public/basic.ics', null, null);
insert into Calendar (dtype, calendar_id, private_id, portlet_instance_id, name, description, url, username, password) values ('CalDAVRemoteCalendar', -4, '2d7428a6-b58c-4008-8575-f05549f16314', '41a3c58a-a9d5-4799-8cf2-08a023ec14ec', 'Google CalDAV', 'Remote CalDAV Calendar description', 'https://www.google.com/calendar/dav/dip.xbasov00%40gmail.com/events', 'dip.xbasov00@gmail.com', 'xbasov00')
insert into Calendar (dtype, calendar_id, private_id, portlet_instance_id, name, description, url, username, password) values ('ICalRemoteCalendar', -5, '2d7428a6-b58c-4008-8575-f05549f16365', '41a3c58a-a9d5-4796-8cf2-08a023ec14ec', 'Shared cal', 'Shared calendar from another portet instance', 'http://localhost:8080/dip-xbasov00/rest/calendars/2d7428a6-b58c-4008-8575-f05549f16366', null, null);
-- [portlet 2]
insert into Calendar (dtype, calendar_id, private_id, portlet_instance_id, name, description, url, username, password) values ('Calendar', -6, '2d7428a6-b58c-4008-8575-f05549f16366', '98f5cfff-7b82-4a12-9f74-01120a79dc8f', 'Osobní', 'Popis osobního kalendáře', null, null, null)
insert into Calendar (dtype, calendar_id, private_id, portlet_instance_id, name, description, url, username, password) values ('ICalRemoteCalendar', -7, '2d7428a6-b58c-4008-8575-f05549f1h367', '98f5cfff-7b82-4a12-9f74-01120a79dc8f', 'Moje úkoly', 'Importované úkoly', 'http://localhost:8080/dip-xbasov00/rest/tasks/ical/14810e9c-af4d-4b05-b59e-06526c3fba56', null, null);
insert into Calendar (dtype, calendar_id, private_id, portlet_instance_id, name, description, url, username, password) values ('Calendar', -8, '2d7428a9-b58c-4008-8575-f05549f1h368', '98f5cfff-7b82-4a12-9f74-01120a79dc8f', 'Fotbalový klub', 'Popis kalendáře fotbalový klub.', null, null, null);


-- EVENT
-- [portlet 1]
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-1, -1, '20130314T135502Z--1@dip.xbasov00.cz', 'Event 1', 'Event description 1', 'Brno', '2013-05-01 12:00:00', '2013-05-04 12:00:00', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-2, -1, '20130314T135502Z--2@dip.xbasov00.cz', 'Event 2', 'Event description 2', 'Brno', '2013-05-03 08:45:00', '2013-05-07 16:30:00', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-3, -1, '20130314T135502Z--3@dip.xbasov00.cz', 'Event 3', 'Event description 3', 'Brno', '2013-05-06 20:10:00', '2013-05-10 12:15:00', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-4, -1, '20130314T135502Z--4@dip.xbasov00.cz', 'Event 4', 'Event description 4', 'Brno', '2013-05-09 06:30:00', '2013-05-13 12:45:00', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-5, -2, '20130314T135502Z--5@dip.xbasov00.cz', 'Event 5', 'Event description 5', 'Brno', '2013-05-12 20:10:00', '2013-05-16 10:15:30', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-7, -2, '20130314T135502Z--7@dip.xbasov00.cz', 'Event 7', 'Event description 7', 'Brno', '2013-05-15 10:00:00', '2013-05-15 11:00:00', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-8, -2, '20130314T135502Z--8@dip.xbasov00.cz', 'Event 8', 'Event description 8', 'Brno', '2013-05-15 14:00:00', '2013-05-15 17:00:00', false)
-- [portlet 2]
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-9, -6, '20130331T135502Z--9@dip.xbasov00.cz', 'Schůzka s Haničkou', 'Event description 9', 'Brno', '2013-06-18 14:00:00', '2013-06-18 17:00:00', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-10, -6, '20130331T135502Z--10@dip.xbasov00.cz', 'IT seminář 1/2', 'Event description 9', 'Brno', '2013-06-18 08:00:00', '2013-06-18 09:30:00', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-11, -6, '20130331T135502Z--11@dip.xbasov00.cz', 'IT seminář 2/2', 'Event description 9', 'Brno', '2013-06-18 10:00:00', '2013-06-18 11:50:00', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-12, -8, '20130331T135502Z--12@dip.xbasov00.cz', 't1', 'Event description 1', 'Brno', '2013-06-05 14:00:00', '2013-06-11 17:00:00', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-13, -8, '20130331T135502Z--13@dip.xbasov00.cz', 't2', 'Event description 2', 'Brno', '2013-06-08 14:00:00', '2013-06-08 17:00:00', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-14, -8, '20130331T135502Z--14@dip.xbasov00.cz', 't3', 'Event description 3', 'Brno', '2013-06-23 14:00:00', '2013-06-23 17:00:00', false)
insert into Event (id, calendar_id, ical_comp_uid, name, description, location, datetime_from, datetime_to, all_day_event) values (-15, -8, '20130331T135502Z--15@dip.xbasov00.cz', 't4', 'Event description 4', 'Brno', '2013-06-25 14:00:00', '2013-06-27 17:00:00', false)

------------------------------------------------------------------
-- CONTACTS PORTLET
------------------------------------------------------------------

-- CONTACT
-- [portlet 3]
insert into Contact (id, private_id, portlet_instance_id, gender, degree_before, name, surname, nick_name, birthday, anniversary) values (-1, '2d7428a6-b58c-4008-8575-f05549f16301', 'd2d2b315-1554-412a-bb98-b18224c12c1a', 0, 'Bc.', 'Martin', 'Basovnik', 'Bazz','1988-11-20', '2012-02-24')
insert into Contact (id, private_id, portlet_instance_id, gender, degree_before, name, surname) values (-2, '2d7428a6-b58c-4008-8575-f05549f16302', 'd2d2b315-1554-412a-bb98-b18224c12c1a', 0, 'Ing.', 'David', 'Novák')
insert into Contact (id, private_id, portlet_instance_id, gender, name, surname) values (-3, '2d7428a6-b58c-4008-8575-f05549f16303', 'd2d2b315-1554-412a-bb98-b18224c12c1a', 0, 'Petr', 'Novotný')
insert into Contact (id, private_id, portlet_instance_id, gender, degree_before, name, surname, nick_name, birthday) values (-4, '2d7428a6-b58c-4008-8575-f05549f16304', 'd2d2b315-1554-412a-bb98-b18224c12c1a', 1, 'Mgr.', 'Eva', 'Ostrá', 'Opice','1987-09-10')
insert into Contact (id, private_id, portlet_instance_id, gender, degree_before, name, surname) values (-5, '2d7428a6-b58c-4008-8575-f05549f16305', 'd2d2b315-1554-412a-bb98-b18224c12c1a', 0, 'Ing.', 'Lukáš', 'Mareček')
insert into Contact (id, private_id, portlet_instance_id, gender, degree_before, name, surname, birthday) values (-6, '2d7428a6-b58c-4008-8575-f05549f16306', 'd2d2b315-1554-412a-bb98-b18224c12c1a', 1, 'Mgr.', 'Jitka', 'Nesmělá', '1990-3-27')
insert into Contact (id, private_id, portlet_instance_id, gender, name, surname, nick_name) values (-7, '2d7428a6-b58c-4008-8575-f05549f16307', 'd2d2b315-1554-412a-bb98-b18224c12c1a', 0, 'Jan', 'Dudek', 'Dudák')
insert into Contact (id, private_id, portlet_instance_id, gender, degree_before, name, surname) values (-8, '2d7428a6-b58c-4008-8575-f05549f16308', 'd2d2b315-1554-412a-bb98-b18224c12c1a', 0, 'Mgr.', 'Filip', 'Zdráhal')
-- [portlet 4]
insert into Contact (id, private_id, portlet_instance_id, gender, degree_before, name, surname, birthday) values (-9, '2d7428a6-b58c-4008-8575-f05549f16309', '9ed99a7f-cf30-4428-9717-6d8be5ee1de7', 1, 'Mgr.', 'Marie', 'Nesmělá', '1990-3-27')
insert into Contact (id, private_id, portlet_instance_id, gender, name, surname, nick_name) values (-10, '2d7428a6-b58c-4008-8575-f05549f16310', '9ed99a7f-cf30-4428-9717-6d8be5ee1de7', 0, 'Marek', 'Dudek', 'Dudák')
insert into Contact (id, private_id, portlet_instance_id, gender, degree_before, name, surname) values (-11, '2d7428a6-b58c-4008-8575-f05549f16311', '9ed99a7f-cf30-4428-9717-6d8be5ee1de7', 0, 'Mgr.', 'Josef', 'Janoušek')

-- LANG
insert into Lang (id, name, code) values (-1, 'Czech', 'cs')
insert into Lang (id, name, code) values (-2, 'English', 'en')
insert into Lang (id, name, code) values (-3, 'French', 'fr')
insert into Lang (id, name, code) values (-4, 'German', 'de')
insert into Lang (id, name, code) values (-5, 'Slovak', 'sk')
insert into Lang (id, name, code) values (-6, 'Spanish', 'es')

-- CONTACT_LANG
insert into Contact_Lang (contact_id, lang_id) values (-1, -1)
insert into Contact_Lang (contact_id, lang_id) values (-1, -2)

-- ADDRESS
insert into Address (id, title, city, po_box, street, zip_code, contact_id) values (-1, 'home', 'Jimramov', '293', 'Padělek', '592 42', -1)
insert into Address (id, title, city, po_box, street, zip_code, contact_id) values (-2, 'Brno', 'Brno-Královo Pole', '2255/40', 'Purkyňova', '61200', -1)

-- EMAIL
insert into Email (id, email, name, contact_id) values (-1, 'xbasov00@stud.fit.vutbr.cz', 'school', -1)
insert into Email (id, email, name, contact_id) values (-2, 'martin.basovnik@gmail.com', 'public', -1)

-- TELEPHONE_NUMBER
insert into Telephone_Number (id, telephone_number, name, contact_id) values (-1, '+420 123 456 789', 'private', -1)
insert into Telephone_Number (id, telephone_number, name, contact_id) values (-2, '+420 987 654 321', 'job', -1)

-- URL
insert into URL (id, url, name, contact_id) values (-1, 'http://www.jboss.org/', 'jboss homepage', -1)
insert into URL (id, url, name, contact_id) values (-2, 'http://www.jboss.org/gatein/', 'gatein homepage', -1)

-- CONTACT_GROUP
-- [portlet 3]
insert into Contact_Group (id, private_id, portlet_instance_id, name) values (-1, '2d7428a6-b58c-4008-8575-f05549f16301', 'd2d2b315-1554-412a-bb98-b18224c12c1a', 'School')
insert into Contact_Group (id, private_id, portlet_instance_id, name) values (-2, '2d7428a6-b58c-4008-8575-f05549f16302', 'd2d2b315-1554-412a-bb98-b18224c12c1a', 'Job')
insert into Contact_Group (id, private_id, portlet_instance_id, name) values (-3, '2d7428a6-b58c-4008-8575-f05549f16303', 'd2d2b315-1554-412a-bb98-b18224c12c1a', 'Friends')
-- [portlet 4]
insert into Contact_Group (id, private_id, portlet_instance_id, name) values (-4, '2d7428a6-b58c-4008-8575-f05549f16304', '9ed99a7f-cf30-4428-9717-6d8be5ee1de7', 'FIT VUT')
insert into Contact_Group (id, private_id, portlet_instance_id, name) values (-5, '2d7428a6-b58c-4008-8575-f05549f16305', '9ed99a7f-cf30-4428-9717-6d8be5ee1de7', 'RedHat')
insert into Contact_Group (id, private_id, portlet_instance_id, name) values (-6, '2d7428a6-b58c-4008-8575-f05549f16306', '9ed99a7f-cf30-4428-9717-6d8be5ee1de7', 'Brno')

-- CONTACT_CONTACTGROUP
insert into Contact_Contactgroup (contact_id, contactgroup_id) values (-2, -1)
insert into Contact_Contactgroup (contact_id, contactgroup_id) values (-6, -2)
insert into Contact_Contactgroup (contact_id, contactgroup_id) values (-7, -2)
insert into Contact_Contactgroup (contact_id, contactgroup_id) values (-8, -2)
insert into Contact_Contactgroup (contact_id, contactgroup_id) values (-4, -3)
insert into Contact_Contactgroup (contact_id, contactgroup_id) values (-6, -3)
insert into Contact_Contactgroup (contact_id, contactgroup_id) values (-9, -4)
insert into Contact_Contactgroup (contact_id, contactgroup_id) values (-10, -5)
insert into Contact_Contactgroup (contact_id, contactgroup_id) values (-11, -6)


------------------------------------------------------------------
-- TASKS PORTLET
------------------------------------------------------------------

-- PROJECT
-- [portlet 5]
insert into Project(id, private_id, portlet_instance_id, name, description, datetime_start) values(-1, '2d7428a6-b58c-4008-8575-f05549f16001', '14810e9c-af4d-4b05-b59e-06526c3fba56', 'Project A', 'Description A', '2013-05-01 10:00:00')
insert into Project(id, private_id, portlet_instance_id, name, description, datetime_start) values(-2, '2d7428a6-b58c-4008-8575-f05549f16002', '14810e9c-af4d-4b05-b59e-06526c3fba56', 'Project B', 'Description B', '2013-05-02 10:00:00')
insert into Project(id, private_id, portlet_instance_id, name, description, datetime_start) values(-3, '2d7428a6-b58c-4008-8575-f05549f16003', '14810e9c-af4d-4b05-b59e-06526c3fba56', 'Project C', 'Description C', '2013-05-03 10:00:00')
-- [portlet 6]
insert into Project(id, private_id, portlet_instance_id, name, description, datetime_start) values(-4, '2d7428a6-b58c-4008-8575-f05549f16004', '2611c6dc-cef4-4112-8433-417f99109cd3', 'Project X', 'Description X', '2013-05-04 10:00:00')


-- TASK
-- [portlet 5]
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task) values(-1, '2d7428a6-b58c-4008-8575-f05549f16001', '14810e9c-af4d-4b05-b59e-06526c3fba56', '20130314T135502Z--1@dip.xbasov00.cz', 'Task 1', 'Description 1', 5, -1, 'INBOX', '2013-06-05 10:00:00', false)
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task) values(-2, '2d7428a6-b58c-4008-8575-f05549f16002', '14810e9c-af4d-4b05-b59e-06526c3fba56', '20130314T135502Z--2@dip.xbasov00.cz', 'Task 2', 'Description 2', 5, -1, 'INBOX', '2013-06-10 12:00:00', false)
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task) values(-3, '2d7428a6-b58c-4008-8575-f05549f16003', '14810e9c-af4d-4b05-b59e-06526c3fba56', '20130314T135502Z--3@dip.xbasov00.cz', 'Task 3', 'Description 3', 5, -1, 'INBOX', '2013-06-15 14:00:00', false)
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task) values(-4, '2d7428a6-b58c-4008-8575-f05549f16004', '14810e9c-af4d-4b05-b59e-06526c3fba56', '20130314T135502Z--4@dip.xbasov00.cz', 'Send all letters', 'Description 4', 5, -1, 'INBOX', '2013-06-20 10:00:00', false)
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task) values(-5, '2d7428a6-b58c-4008-8575-f05549f16005', '14810e9c-af4d-4b05-b59e-06526c3fba56', '20130314T135502Z--5@dip.xbasov00.cz', 'Seminar registration!', 'Description 5', 5, -2, 'INBOX', '2013-06-20 19:30:00', false)
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task, datetime_scheduled) values(-6, '2d7428a6-b58c-4008-8575-f05549f16006', '14810e9c-af4d-4b05-b59e-06526c3fba56', '20130314T135502Z--6@dip.xbasov00.cz', 'Buy some food', 'Description 6', 5, -2, 'SCHEDULED', '2013-05-09 10:00:00', false, '2013-06-05 16:00:00')
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task, datetime_scheduled) values(-7, '2d7428a6-b58c-4008-8575-f05549f16007', '14810e9c-af4d-4b05-b59e-06526c3fba56', '20130314T135502Z--7@dip.xbasov00.cz', 'Send email to David', 'Description 7', 5, -2, 'SCHEDULED', '2013-05-09 14:00:00', false, '2013-06-05 17:00:00')
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task, datetime_scheduled) values(-8, '2d7428a6-b58c-4008-8575-f05549f16008', '14810e9c-af4d-4b05-b59e-06526c3fba56', '20130314T135502Z--8@dip.xbasov00.cz', 'Call home', 'Description 8', 5, -2, 'SCHEDULED', '2013-05-09 20:00:00', false, '2013-06-05 18:00:00')
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task, datetime_scheduled) values(-9, '2d7428a6-b58c-4008-8575-f05549f16009', '14810e9c-af4d-4b05-b59e-06526c3fba56', '20130314T135502Z--9@dip.xbasov00.cz', 'Check email', 'Description 9', 5, -4, 'SCHEDULED', '2013-05-09 21:00:00', false, '2013-06-10 19:00:00')
-- [portlet 6]
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task, datetime_scheduled) values(-10, '2d7428a6-b58c-4008-8575-f05549f16010', '2611c6dc-cef4-4112-8433-417f99109cd3', '20130314T135502Z--10@dip.xbasov00.cz', 'Task 10', 'Description 10', 5, -4, 'SCHEDULED', '2013-04-01 20:00:00', false, '2013-06-15 20:00:00')
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task, datetime_scheduled) values(-11, '2d7428a6-b58c-4008-8575-f05549f16011', '2611c6dc-cef4-4112-8433-417f99109cd3', '20130314T135502Z--11@dip.xbasov00.cz', 'Poslat všechny dopisy', 'Description 11', 5, -4, 'SCHEDULED', '2013-04-01 21:00:00', false, '2013-06-18 10:00:00')
insert into Task(id, private_id, portlet_instance_id, vtodo_uid, name, description, priority, project_id, gtd_group, datetime_created, all_day_task, datetime_scheduled) values(-12, '2d7428a6-b58c-4008-8575-f05549f16012', '2611c6dc-cef4-4112-8433-417f99109cd3', '20130314T135502Z--12@dip.xbasov00.cz', 'Registrace semináře!', 'Description 12', 5, -4, 'SCHEDULED', '2013-04-01 22:00:00', false, '2013-06-18 20:00:00')

-- CONTEXT
-- [portlet 5]
insert into Context(id, private_id, portlet_instance_id, name) values(-1, '2d7428a6-b58d-4008-8575-f05549f16001', '14810e9c-af4d-4b05-b59e-06526c3fba56', 'School')
insert into Context(id, private_id, portlet_instance_id, name) values(-2, '2d7428a6-b58d-4008-8575-f05549f16002', '14810e9c-af4d-4b05-b59e-06526c3fba56', 'Work')
insert into Context(id, private_id, portlet_instance_id, name) values(-3, '2d7428a6-b58d-4008-8575-f05549f16003', '14810e9c-af4d-4b05-b59e-06526c3fba56', 'PC.')
-- [portlet 6]
insert into Context(id, private_id, portlet_instance_id, name) values(-4, '2d7428a6-b58d-4008-8575-f05549f16004', '2611c6dc-cef4-4112-8433-417f99109cd3', 'DIP')


-- TASK_CONTEXT
insert into Task_Context(task_id, context_id) values (-1, -1)
insert into Task_Context(task_id, context_id) values (-1, -2)
insert into Task_Context(task_id, context_id) values (-1, -3)
insert into Task_Context(task_id, context_id) values (-2, -1)
insert into Task_Context(task_id, context_id) values (-3, -1)
insert into Task_Context(task_id, context_id) values (-4, -2)
insert into Task_Context(task_id, context_id) values (-5, -3)

-- ========================================================================

