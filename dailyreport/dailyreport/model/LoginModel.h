

#import <Foundation/Foundation.h>

@protocol LoginDelegate;
@protocol CheckSessionDelegate;
@class User;

@interface LoginModel : TTURLRequestModel {
    id<LoginDelegate> _delegate;
    id<CheckSessionDelegate> _checkSessionDelegate;
    User* _user;
}

@property(nonatomic,assign) id<LoginDelegate> delegate;
@property(nonatomic,assign) id<CheckSessionDelegate> checkSessionDelegate;
@property (nonatomic, readonly) User* user;

//- (id)initWithDelegate:(id /*<LoginDelegate>*/)delegate;
- (void)login:(NSString*)username password:(NSString*)password;
- (void)checkSession;
@end
@protocol CheckSessionDelegate <NSObject>
- (void)checkSessionResult:(BOOL) result;
@end
@protocol LoginDelegate <NSObject>

- (void)loginStart:(User*) user;
- (void)loginSuccess:(User*) user;
- (void)loginFailed:(NSError*) error message:(NSString*) message; 

@end
