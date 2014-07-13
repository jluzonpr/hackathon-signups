package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import com.google.gson.Gson
import database.Dao
import database.dto.UserInfo
import database.json.UserInfoJson
import database.json.SkillJson
import database.json.UniversityJson
import database.json.EventJson

object Gets extends Controller {
	private val gson = new Gson();

	def usersAll = Action {
		val users = Dao.getUserInfo(0, 20).toArray;
		Ok(gson.toJson(users));
	}
	def users(id: Int) = Action {
		val userInfoOpt = Dao.getUserInfo(id);
		val userInfo = userInfoOpt.getOrElse(new UserInfo());
		val userInfoJson = new UserInfoJson(userInfo);

		Ok(gson.fromJson(userInfoJson.toString(),classOf[UserInfoJson]).toString()).as("application/json");
	}
	def userSkills(userId: Int) = Action {
		val skills = Dao.getSkillsByUser(userId);
		val jsonArr = new Array[SkillJson](skills.size);
		for(i <- 0 until skills.size) {
			jsonArr(i) = new SkillJson(skills(i));
		}
		Ok(gson.toJson(jsonArr)).as("application/json");
	}
	def userMe = Action {
		request => 
		try {
			val headersMap = request.headers.toMap;
			val token = headersMap.getOrElse("token", Seq(""))(0);
			val user = Dao.getLoginSession(token).getOrElse(throw new Exception("BOOM")).getUser();
			val userInfoJson = new UserInfoJson(user.getUserInfo());
			Ok(userInfoJson.toString());
		}
		catch {
		case e: Exception => { e.printStackTrace(); InternalServerError("Something went wrong...") }
		}
	}
	def logout = Action {
		request => 
		try {
			val headersMap = request.headers.toMap;
			val token = headersMap.getOrElse("token", Seq(""))(0);
			val loginSessionOpt = Dao.getLoginSession(token);
			if(loginSessionOpt.isDefined) {
				Dao.deleteSessionToken(loginSessionOpt.get.getToken());
			}
			Ok("Logged out successfully.");
		}
		catch {
		case e: Exception => { e.printStackTrace(); InternalServerError("Something went wrong...") }
		}
	}
	def universities = Action {
		request => 
		try {
			val headersMap = request.headers.toMap;
			val token = headersMap.getOrElse("token", Seq(""))(0);
			val sessionOpt = Dao.getLoginSession(token);
			if(sessionOpt.isEmpty) {
				throw new Exception("No active session found.");
			}
			val universities = Dao.getUniversities();
			val univertiesArr = new Array[UniversityJson](universities.size);
			for(i <- 0 until univertiesArr.size) {
				univertiesArr(i) = new UniversityJson(universities(i));
			}
			Ok(gson.toJson(univertiesArr)).as("application/json");

		}
		catch {
		case e: Exception => { e.printStackTrace(); InternalServerError("Something went wrong...") }
		}
	}
	def event(id: Int) = Action {
		request => 
		try {
			val headersMap = request.headers.toMap;
			val token = headersMap.getOrElse("token", Seq(""))(0);
			val sessionOpt = Dao.getLoginSession(token);
			if(sessionOpt.isEmpty) {
				throw new Exception("No active session found.");
			}
			val event = Dao.getEvent(id).getOrElse(throw new NoSuchElementException("Event not found."));
			val eventJson = new EventJson(event);
			Ok(gson.toJson(eventJson)).as("application/json");

		}
		catch {
		  case e: NoSuchElementException => NotFound(e.getMessage());
		case e: Exception => { e.printStackTrace(); InternalServerError("Something went wrong...") }
		}
	}
	def eventAttendees(id:Int) = Action {
	  NotFound("Nothing here yet.");
	}

}