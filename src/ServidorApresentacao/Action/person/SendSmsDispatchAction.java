/*
 * Created on 31/Mai/2004
 *  
 */
package ServidorApresentacao.Action.person;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.sms.SmsLimitReachedServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 *  
 */
public class SendSmsDispatchAction extends FenixDispatchAction
{

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		IUserView userView = SessionUtils.getUserView(request);

		Integer remainingSmsNumber = null;

		Date startDate = getStartMonthDate();
		Date endDate = getEndMonthDate();

		Object args[] = { userView, startDate, endDate };
		try
		{
			remainingSmsNumber =
				(Integer) ServiceUtils.executeService(
					userView,
					"CountSentSmsByPersonAndDatePeriod",
					args);

		}
		catch (SmsLimitReachedServiceException e)
		{
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add("smsLimitReached", new ActionError(e.getMessage()));
			saveErrors(request, actionErrors);
			return mapping.findForward("error");
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
		}

		ActionMessages messages = new ActionMessages();
		messages.add("message1", new ActionMessage("message.person.remainingSms", remainingSmsNumber));
		saveMessages(request, messages);

		return mapping.findForward("start");

	}

	public ActionForward send(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm sendSmsForm = (DynaActionForm) form;

		String message = (String) sendSmsForm.get("message");
		Integer destinationPhoneNumber = (Integer) sendSmsForm.get("destinationPhoneNumber");

		Date startDate = getStartMonthDate();
		Date endDate = getEndMonthDate();

		Object args[] = { userView, startDate, endDate, destinationPhoneNumber, message };

		try
		{
			ServiceUtils.executeService(userView, "CreateSentSms", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
		}

		ActionMessages messages = new ActionMessages();
		messages.add("message1", new ActionMessage("message.person.sendSmsSuccess"));
		saveMessages(request, messages);

		sendSmsForm.set("message", "");
		sendSmsForm.set("destinationPhoneNumber", null);
		return mapping.findForward("error");

	}

	private Date getStartMonthDate()
	{
		Calendar cal = new GregorianCalendar();
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		Calendar startCalendar = new GregorianCalendar(year, month, 1);

		return startCalendar.getTime();
	}

	private Date getEndMonthDate()
	{
		Calendar cal = new GregorianCalendar();

		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		if (month == Calendar.DECEMBER)
		{
			month = Calendar.JANUARY;
			year += 1;
		}
		else
		{
			month += 1;
		}

		Calendar endCalendar = new GregorianCalendar(year, month, 1);

		return endCalendar.getTime();

	}
}
