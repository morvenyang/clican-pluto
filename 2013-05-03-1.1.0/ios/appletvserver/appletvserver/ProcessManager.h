//
//  ProcessManager.h
//  appletvserver
//
//  Created by zhang wei on 13-4-12.
//
//

#import <Foundation/Foundation.h>

@interface ProcessManager : NSObject

+(void) changeRunningProcess:(NSString*) process;
+(void) stopMkv;
@end
