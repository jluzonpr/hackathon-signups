package database

import java.util.Date

import cryptography.PasswordHash
import database.dto.{Event, EventAttendance, LoginSession, Skill, SkillCategory, User}
import org.hibernate.criterion.Restrictions

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

object Dao {

	private val hibernateService = Database.HibernateService;
	private val SessionHashByteSize = 32;
	//Users
	def getUser(userId: Int): Option[User] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[User])
					.add(Restrictions.eq(User.ID, userId))
					.setCacheable(true);;
					val user = criteria.uniqueResult().asInstanceOf[User];
					hibernateService.closeSessionIfNecessary(session);
					Option(user)
	}
	def getUser(email: String): Option[User] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[User])
					.add(Restrictions.eq(User.EMAIL, email))
					.setCacheable(true);
			val user = criteria.uniqueResult().asInstanceOf[User];
			hibernateService.closeSessionIfNecessary(session);
			Option(user)
	}
	def getUsers(startingAt: Int, maxAmount: Int): List[User] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[User])
		  .setMaxResults(maxAmount)
		  .setFirstResult(startingAt);
			val usersList = criteria.list();
			val users = usersList.asInstanceOf[java.util.List[User]].asScala.toList;
			hibernateService.closeSessionIfNecessary(session);
			users;
	}
	def updateUser(user: User) {
		val session = hibernateService.getCurrentSession(true);
		val transaction = session.beginTransaction();
		session.update(user);
		transaction.commit();
		hibernateService.closeSessionIfNecessary(session);
	}

	//	def getUserInfo(userId: Int): Option[User] = {
	//			val session = hibernateService.getCurrentSession(true);
	//			val criteria = session.createCriteria(classOf[UserInfo]);
	//			criteria.add(Restrictions.eq(User.USER_ID, userId));
	//			val User = criteria.uniqueResult().asInstanceOf[UserInfo];
	//			hibernateService.closeSessionIfNecessary(session);
	//			Option(userInfo);
	//	}
	//	def getUserInfo(startingAt: Int, maxAmount: Int): List[User] = {
	//			val session = hibernateService.getCurrentSession(true);
	//			val criteria = session.createCriteria(classOf[UserInfo]);
	//			criteria.setMaxResults(maxAmount).setFirstResult(startingAt);
	//			val usersInfoList = criteria.list();
	//			val usersInfo = usersInfoList.asInstanceOf[java.util.List[UserInfo]].asScala.toList;
	//			hibernateService.closeSessionIfNecessary(session);
	//			usersInfo;
	//	}


	//Events
	def getEvent(eventId: Int): Option[Event] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[Event])
					.add(Restrictions.eq(Event.ID, eventId))
					.setCacheable(true);
			val event = criteria.uniqueResult().asInstanceOf[Event];
			hibernateService.closeSessionIfNecessary(session);
			Option(event)
	}
	def getEventsInRange(fromStartTime: Date, untilEndTime: Date, startingAt: Int, maxAmount: Int): List[Event] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[Event])
					.setMaxResults(maxAmount)
					.setFirstResult(startingAt)
					.add(Restrictions.ge(Event.START_TIME, fromStartTime))
					.add(Restrictions.le(Event.END_TIME, untilEndTime));
			val events = criteria.list().asInstanceOf[java.util.List[Event]].asScala.toList;
			hibernateService.closeSessionIfNecessary(session);
			events;
	}
	def getEventsAfter(fromStartTime: Date, startingAt: Int, maxAmount: Int): List[Event] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[Event])
					.setMaxResults(maxAmount)
					.setFirstResult(startingAt)
					.add(Restrictions.ge(Event.START_TIME, fromStartTime));
			val events = criteria.list().asInstanceOf[java.util.List[Event]].asScala.toList;
			hibernateService.closeSessionIfNecessary(session);
			events;
	}
	//Event Attendance
	def getEventAttendance(eventId: Int): List[EventAttendance] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[EventAttendance],"attendance")
					.createAlias("attendance.event", "event")
					.add(Restrictions.eq("event.eventId", eventId));
			val eventAttendanceList = criteria.list().asInstanceOf[java.util.List[EventAttendance]].asScala.toList;
			hibernateService.closeSessionIfNecessary(session);
			eventAttendanceList;
	}
	def getEventAttendee(eventId: Int, userId: Int): Option[EventAttendance] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[EventAttendance])
					.add(Restrictions.eq(EventAttendance.EVENT_ID,eventId))
					.add(Restrictions.eq(EventAttendance.USER_ID, userId))
					.setCacheable(true);
			val eventAttendee = criteria.uniqueResult().asInstanceOf[EventAttendance];
			hibernateService.closeSessionIfNecessary(session);
			Option(eventAttendee);
	}
	//LoginSessions
	def getLoginSessions(userId: Int): List[LoginSession] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[LoginSession])
					.add(Restrictions.eq(LoginSession.USER + ".userId", userId))
					.setCacheable(true);
			val eventAttendanceList = criteria.list().asInstanceOf[java.util.List[LoginSession]].asScala.toList;
			hibernateService.closeSessionIfNecessary(session);
			eventAttendanceList;
	}
	def getLoginSessions(email: String): List[LoginSession] = {
			val listBuffer = new ListBuffer[LoginSession]();

			listBuffer.toList;
	}
	def getLoginSession(token: String):Option[LoginSession] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[LoginSession])
					.add(Restrictions.eq(LoginSession.TOKEN, token))
					.setCacheable(true);
			val loginSession = criteria.uniqueResult().asInstanceOf[LoginSession];
			hibernateService.closeSessionIfNecessary(session);
			Option(loginSession);
	}
	//Skills
	def getSkills(): List[Skill] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[Skill]);
			val skills = criteria.list().asInstanceOf[java.util.List[Skill]].asScala.toList;
			hibernateService.closeSessionIfNecessary(session);
			skills;
	}
	def getSkillCategories(): List[SkillCategory] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[SkillCategory]);
			val skillCategories = criteria.list().asInstanceOf[java.util.List[SkillCategory]].asScala.toList;
			hibernateService.closeSessionIfNecessary(session);
			skillCategories;
	}
	def getSkillsByCategory(categoryId: Int): List[Skill] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[Skill])
					.add(Restrictions.eq(Skill.SKILL_CATEGORY_ID, categoryId));
			val skills = criteria.list().asInstanceOf[java.util.List[Skill]].asScala.toList;
			hibernateService.closeSessionIfNecessary(session);
			skills;
	}
	def getSkillsByCategory(category: SkillCategory): List[Skill] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[Skill])
					.add(Restrictions.eq(Skill.SKILL_CATEGORY_ID, category.getId()));
			val skills = criteria.list().asInstanceOf[java.util.List[Skill]].asScala.toList;
			hibernateService.closeSessionIfNecessary(session);
			skills;
	}
//	def getSkillsByUser(userId:Int): List[Skill] = {
//			val user = getUser(userId).getOrElse(return List[Skill]());
//			val skills = user.getSkills();
//			return skills.asScala.toList;
//	}
	//Skill Cats
	def getSkillCategory(id: Int): Option[SkillCategory] = {
			val session = hibernateService.getCurrentSession(true);
			val criteria = session.createCriteria(classOf[SkillCategory])
					.add(Restrictions.eq(SkillCategory.ID, id));
			val skillCategory = criteria.uniqueResult().asInstanceOf[SkillCategory];
			hibernateService.closeSessionIfNecessary(session);
			Option(skillCategory);
	}
	def login(email: String, password: String): Boolean = {
			val user = Dao.getUser(email).getOrElse(return false);
			val passwordsMatch = PasswordHash.validatePassword(password, user.getPasswordHash());
			passwordsMatch;

	}
	def register(email: String, password: String): Option[User] = {
			val user = new User();
			user.setEmail(email);
			val hashedPassword = PasswordHash.createHash(password);
			user.setPasswordHash(hashedPassword);
			val session = hibernateService.getCurrentSession(true);
			val transaction = session.beginTransaction();
			session.save(user);
			transaction.commit();
			hibernateService.closeSessionIfNecessary(session);
			Option(user);

	}
	def addSessionTokenToUser(userId: Int): Option[LoginSession] = {
			val user = getUser(userId).getOrElse(return None);
			val loginSession = new LoginSession();
			loginSession.setUser(user);
			val sessionToken = PasswordHash.createRandomString(SessionHashByteSize);
			loginSession.setToken(sessionToken);
			val session = hibernateService.getCurrentSession(true);
			val transaction = session.beginTransaction();
			session.save(loginSession);
			transaction.commit();
			session.close();
			Option(loginSession);
	}
	def deleteSessionToken(token: String): Boolean = {
			val loginSession = getLoginSession(token).getOrElse(return false);
			val session = hibernateService.getCurrentSession(true);
			val transaction = session.beginTransaction();
			session.delete(loginSession);
			transaction.commit();
			session.close();
			true;	
	}
	def addEvent(name: String, startTime: Date, endTime: Date): Option[Event] = {
			if(startTime.after(endTime)) {
				return None;			
			}
			if(startTime.before(new Date())) { //if start time before now
				return None;
			} 
			if(name.isEmpty()) {
				return None;
			}
			val event = new Event();
			event.setName(name);
			event.setStartTime(startTime);
			event.setEndTime(endTime);
			val session = hibernateService.getCurrentSession(true);
			val transaction = session.beginTransaction();
			val id = session.save(event);
			transaction.commit();
			session.close();
			//			getEvent(id.toString.toInt);
			Option(event);
	}
	def attendEvent(eventId: Int, userId: Int): Option[EventAttendance]  = {

			val user = getUser(userId).getOrElse(return None);
			val event = getEvent(eventId).getOrElse(return None);
			val eventAttendee = new EventAttendance();
			eventAttendee.setEvent(event);
			eventAttendee.setUser(user);
			val session = hibernateService.getCurrentSession(true);
			try {
				val transaction = session.beginTransaction();
				session.save(eventAttendee);
				transaction.commit();
				Option(eventAttendee);
			}
			finally {
				session.close();
			}
	}
	def changePassword(userId: Int, newPassword: String): Boolean = {
			val user = getUser(userId).getOrElse(return false);
			user.setPasswordHash(PasswordHash.createHash(newPassword));
			val session = hibernateService.getCurrentSession(true);
			val transaction = session.beginTransaction();
			session.update(user);
			transaction.commit();
			session.close();
			true;
	}



}	