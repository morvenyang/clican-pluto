

#import <Foundation/Foundation.h>

@protocol LoginDelegate;
@protocol CheckSessionDelegate;
@protocol PasswordDelegate;

@class User;

@interface LoginModel : TTURLRequestModel {
    id<LoginDelegate> _delegate;
    id<CheckSessionDelegate> _checkSessionDelegate;
    id<PasswordDelegate> _passwordDelegate;
    User* _user;
}

@property(nonatomic,assign) id<LoginDelegate> delegate;
@property(nonatomic,assign) id<CheckSessionDelegate> checkSessionDelegate;
@property(nonatomic,assign) id<PasswordDelegate> passwordDelegate;
@property (nonatomic, readonly) User* user;

//- (id)initWithDelegate:(id /*<LoginDelegate>*/)delegate;
- (void)login:(NSString*)username password:(NSString*)password;
- (void)checkSession;
-(void)changePassword:(NSString*)username password:(NSString*)password oldPassword:(NSString*)oldPassword;
@end
@protocol CheckSessionDelegate <NSObject>
- (void)checkSessionResult:(BOOL) result;
@end
@protocol PasswordDelegate <NSObject>
- (void)passwordResult:(NSString*) result success:(BOOL) success;
@end
@protocol LoginDelegate <NSObject>

- (void)loginStart:(User*) user;
- (void)loginSuccess:(User*) user;
- (void)loginFailed:(NSError*) error message:(NSString*) message; 

@end
