

#import <Foundation/Foundation.h>


@interface User : NSObject {
    NSString *_username;
    NSString *_password;
    NSString* _sessionId;
    NSNumber* _expiredDays;
    NSDate* _date;
    NSDate* _oldDate;
    int _timeoutInterval;
    BOOL _showGestureLock;
}

@property (nonatomic, copy) NSString* username;
@property (nonatomic, copy) NSString* password;
@property (nonatomic, copy) NSString* sessionId;
@property (nonatomic, retain) NSNumber* expiredDays;
@property (nonatomic, retain) NSDate* date;
@property (nonatomic, retain) NSDate* oldDate;
@property (nonatomic, assign) int timeoutInterval;
@property (nonatomic, assign) BOOL showGestureLock;
@end
