

#import <Foundation/Foundation.h>


@interface User : NSObject {
    NSString *_username;
    NSString *_password;
    NSString* _sessionId;
    NSNumber* _expiredDays;
}

@property (nonatomic, copy) NSString* username;
@property (nonatomic, copy) NSString* password;
@property (nonatomic, copy) NSString* sessionId;
@property (nonatomic, retain) NSNumber* expiredDays;
@end
