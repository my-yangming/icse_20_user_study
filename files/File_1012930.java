package teammates.ui.webapi.action;

import java.util.List;

import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.EntityNotFoundException;
import teammates.common.util.Const;
import teammates.ui.webapi.output.CourseSectionNamesData;

/**
 * Gets the section names of a course.
 */
public class GetCourseSectionNamesAction extends Action {

    @Override
    protected AuthType getMinAuthLevel() {
        return AuthType.LOGGED_IN;
    }

    @Override
    public void checkSpecificAccessControl() {
        String courseId = getNonNullRequestParamValue(Const.ParamsNames.COURSE_ID);
        CourseAttributes course = logic.getCourse(courseId);
        InstructorAttributes instructor = logic.getInstructorForGoogleId(courseId, userInfo.id);
        gateKeeper.verifyAccessible(instructor, course);
    }

    @Override
    public ActionResult execute() {
        String courseId = getNonNullRequestParamValue(Const.ParamsNames.COURSE_ID);
        try {
            List<String> sectionNames = logic.getSectionNamesForCourse(courseId);
            return new JsonResult(new CourseSectionNamesData(sectionNames));
        } catch (EntityDoesNotExistException e) {
            throw new EntityNotFoundException(e);
        }
    }

}
