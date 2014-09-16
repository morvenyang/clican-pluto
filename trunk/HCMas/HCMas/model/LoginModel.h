

#import <Foundation/Foundation.h>
#import <Three20Network/Three20Network.h>
@protocol LoginDelegate;
@class User;

@interface LoginModel : TTURLRequestModel {
    id<LoginDelegate> _delegate;
    User* _user;
}

@property(nonatomic,assign) id<LoginDelegate> delegate;

@property (nonatomic, readonly) User* user;


- (void)login:(NSString*)username password:(NSString*)password;

@end

@protocol LoginDelegate <NSObject>

- (void)loginStart:(User*) user;
- (void)loginSuccess:(User*) user;
- (void)loginFailed:(NSError*) error message:(NSString*) message; 

@end
