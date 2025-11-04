package com.api.formSync.Email;

import com.api.formSync.Dto.FormResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailTemplate {
    @Value("${BASE_URL}")
    public String BASE_URL;

    public String verificationBody(String name, String token) {
        return """
                <html>
                <head>
                  <meta charset="UTF-8" />
                  <title>Verify Your Email - FormSync</title>
                </head>
                <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: Arial, sans-serif;">
                  <table width="100%" cellspacing="0" cellpadding="0">
                    <tr>
                      <td align="center" style="padding: 40px 0;">
                        <table width="600" cellspacing="0" cellpadding="0" style="background: #ffffff; border-radius: 8px; overflow: hidden; border: 1px solid #e6e6e6;">
                
                          <tr>
                            <td style="background: #111827; text-align: center; padding: 20px;">
                              <img src="https://your-logo-url.png" alt="FormSync Logo" width="120" style="margin-bottom: 5px;" />
                              <h1 style="color: #ffffff; margin: 0; font-size: 22px;">FormSync</h1>
                            </td>
                          </tr>
                
                          <tr>
                            <td style="padding: 30px; color: #333333; font-size: 15px; line-height: 1.6;">
                              <p style="margin-top: 0;">Hi <strong>{{username}}</strong>,</p>
                
                              <p>
                                Thank you for creating an account with <strong>FormSync</strong>. \s
                                To ensure the security of your account, please verify your email address by clicking the button below.
                              </p>
                
                              <div style="text-align: center; margin: 35px 0;">
                                <a href="{{verificationUrl}}"\s
                                   style="background-color: #2563EB; color: #ffffff; padding: 14px 28px; text-decoration: none; border-radius: 6px; font-size: 16px; display: inline-block;">
                                  Verify Email
                                </a>
                              </div>
                
                              <p>
                                If the button above does not work, copy and paste the link below into your web browser:
                              </p>
                
                              <p style="word-break: break-all;">
                                <a href="{{verificationUrl}}" style="color: #2563EB;">{{verificationUrl}}</a>
                              </p>
                
                              <p>
                                If you did not request this account, you may safely ignore this email.
                              </p>
                            </td>
                          </tr>
                
                          <tr>
                            <td style="background: #f9f9f9; text-align: center; padding: 20px; font-size: 13px; color: #666;">
                              © FormSync — All Rights Reserved.
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.replace("{{username}}", name)
                .replace("{{verificationUrl}}", getLink(token));
    }

    public String passwordResetBody(String name, String token) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8" />
                  <title>Reset Your Password - FormSync</title>
                </head>
                <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: Arial, sans-serif;">
                  <table width="100%" cellspacing="0" cellpadding="0">
                    <tr>
                      <td align="center" style="padding: 40px 0;">
                        <table width="600" cellspacing="0" cellpadding="0" style="background: #ffffff; border-radius: 8px; overflow: hidden; border: 1px solid #e6e6e6;">
                
                          <tr>
                            <td style="background: #111827; text-align: center; padding: 20px;">
                              <img src="https://your-logo-url.png" alt="FormSync Logo" width="120" style="margin-bottom: 5px;" />
                              <h1 style="color: #ffffff; margin: 0; font-size: 22px;">FormSync</h1>
                            </td>
                          </tr>
                
                          <tr>
                            <td style="padding: 30px; color: #333333; font-size: 15px; line-height: 1.6;">
                              <p style="margin-top: 0;">Hi <strong>{{username}}</strong>,</p>
                
                              <p>
                                We received a request to reset the password associated with your FormSync account.
                                If you initiated this request, please click the button below to create a new password.
                              </p>
                
                              <div style="text-align: center; margin: 35px 0;">
                                <a href="{{resetUrl}}"
                                   style="background-color: #DC2626; color: #ffffff; padding: 14px 28px; text-decoration: none; border-radius: 6px; font-size: 16px; display: inline-block;">
                                  Reset Password
                                </a>
                              </div>
                
                              <p>
                                If the button above does not work, copy and paste the following link into your browser:
                              </p>
                
                              <p style="word-break: break-all;">
                                <a href="{{resetUrl}}" style="color: #2563EB;">{{resetUrl}}</a>
                              </p>
                
                              <p>
                                If you did not request a password reset, you can ignore this email. Your account will remain secure.
                              </p>
                            </td>
                          </tr>
                
                          <tr>
                            <td style="background: #f9f9f9; text-align: center; padding: 20px; font-size: 13px; color: #666;">
                              © FormSync — All Rights Reserved.
                            </td>
                          </tr>
                
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.replace("{{username}}", name).replace("{{resetUrl}}", getLink(token));
    }

    public String emailChangeBody(String name, String token) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8" />
                  <title>Confirm Email Change - FormSync</title>
                </head>
                <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: Arial, sans-serif;">
                  <table width="100%" cellspacing="0" cellpadding="0">
                    <tr>
                      <td align="center" style="padding: 40px 0;">
                        <table width="600" cellspacing="0" cellpadding="0" style="background: #ffffff; border-radius: 8px; overflow: hidden; border: 1px solid #e6e6e6;">
                
                          <tr>
                            <td style="background: #111827; text-align: center; padding: 20px;">
                              <img src="https://your-logo-url.png" alt="FormSync Logo" width="120" style="margin-bottom: 5px;" />
                              <h1 style="color: #ffffff; margin: 0; font-size: 22px;">FormSync</h1>
                            </td>
                          </tr>
                
                          <tr>
                            <td style="padding: 30px; color: #333333; font-size: 15px; line-height: 1.6;">
                              <p style="margin-top: 0;">Hi <strong>{{username}}</strong>,</p>
                
                              <p>
                                You requested to change the email associated with your FormSync account. \s
                                To confirm and complete this change, please click the button below.
                              </p>
                
                              <div style="text-align: center; margin: 35px 0;">
                                <a href="{{confirmEmailChangeUrl}}"
                                   style="background-color: #2563EB; color: #ffffff; padding: 14px 28px; text-decoration: none; border-radius: 6px; font-size: 16px; display: inline-block;">
                                  Confirm Email Change
                                </a>
                              </div>
                
                              <p>
                                If the button does not work, use the link below:
                              </p>
                
                              <p style="word-break: break-all;">
                                <a href="{{confirmEmailChangeUrl}}" style="color: #2563EB;">{{confirmEmailChangeUrl}}</a>
                              </p>
                
                              <p>
                                If you did not request this change, you can ignore this email.
                              </p>
                            </td>
                          </tr>
                
                          <tr>
                            <td style="background: #f9f9f9; text-align: center; padding: 20px; font-size: 13px; color: #666;">
                              © FormSync — All Rights Reserved.
                            </td>
                          </tr>
                
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.replace("{{username}}", name)
                .replace("{{confirmEmailChangeUrl}}", getLink(token));
    }

    public String formSubmissionBody(FormResponse response) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8" />
                  <title>New Form Submission - FormSync</title>
                </head>
                <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: Arial, sans-serif;">
                  <table width="100%" cellspacing="0" cellpadding="0">
                    <tr>
                      <td align="center" style="padding: 40px 0;">
                        <table width="600" cellspacing="0" cellpadding="0" style="background: #ffffff; border-radius: 8px; overflow: hidden; border: 1px solid #e6e6e6;">
                
                          <tr>
                            <td style="background: #111827; text-align: center; padding: 20px;">
                              <img src="https://your-logo-url.png" alt="FormSync Logo" width="120" style="margin-bottom: 5px;" />
                              <h1 style="color: #ffffff; margin: 0; font-size: 22px;">FormSync</h1>
                            </td>
                          </tr>
                
                          <tr>
                            <td style="padding: 30px; color: #333333; font-size: 15px; line-height: 1.6;">
                              <h2 style="margin-top: 0;">New Form Submission</h2>
                
                              <p>A new form has been submitted on your website. Details are provided below:</p>
                
                              <table width="100%" cellpadding="8" cellspacing="0" style="border-collapse: collapse; margin-top: 15px;">
                                <tr>
                                  <td style="background: #f9f9f9; width: 30%; font-weight: bold;">Name</td>
                                  <td>{{name}}</td>
                                </tr>
                                <tr>
                                  <td style="background: #f9f9f9; font-weight: bold;">Email</td>
                                  <td>{{email}}</td>
                                </tr>
                                <tr>
                                  <td style="background: #f9f9f9; font-weight: bold;">Subject</td>
                                  <td>{{subject}}</td>
                                </tr>
                                <tr>
                                  <td style="background: #f9f9f9; font-weight: bold;">Message</td>
                                  <td>{{message}}</td>
                                </tr>
                              </table>
                
                              <p style="margin-top: 25px;">
                                You can view this form and others in your dashboard on FormSync.
                              </p>
                            </td>
                          </tr>
                
                          <tr>
                            <td style="background: #f9f9f9; text-align: center; padding: 20px; font-size: 13px; color: #666;">
                              © FormSync — All Rights Reserved.
                            </td>
                          </tr>
                
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.replace("{{name}}", response.getName())
                .replace("{{email}}", response.getEmail())
                .replace("{{subject}}", response.getSubject())
                .replace("{{message}}", response.getMessage());
    }

    public String getLink(String token) {
        return BASE_URL + "/verify?token=" + token;
    }
}