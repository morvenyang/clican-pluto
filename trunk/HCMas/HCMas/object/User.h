

#import <Foundation/Foundation.h>

#import "Project.h"
@interface User : NSObject {
    NSString *_username;
    NSString *_password;
    NSString* _sessionId;
    Project* _selectedProject;
    NSString* _appName;
    NSString* _cr;
}

@property (nonatomic, copy) NSString* username;
@property (nonatomic, copy) NSString* password;
@property (nonatomic, copy) NSString* sessionId;
@property (nonatomic, retain) Project* selectedProject;
@property (nonatomic, copy)  NSString* appName;
@property (nonatomic, copy) NSString* cr;
@end
