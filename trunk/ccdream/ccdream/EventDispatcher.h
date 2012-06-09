//
//  EventDispatcher.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"


@interface EventDispatcher : NSObject {
    
}

-(void) dispatch:(long) sessionId forState:(long) stateId forEventType:(NSString*) eventType forParameters:(NSDictionary*) parameters;

@end
