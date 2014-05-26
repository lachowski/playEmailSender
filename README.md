playEmailSender
===============

Project shows how to send email with Apache Common Email and Playframework 2

Configuration
--------------------

File conf/application.conf contains additional properties related to email server (in this example gmail server)

	email.hostname=smtp.googlemail.com
	email.smtpport=465
	email.username=user
	email.password=password
	email.sslonconnect=true
	
	
Types of email messages
--------------------

sendMail method  is able to send simple message with plain text, simple message with plain text and attachments, html messages with and without attachments.