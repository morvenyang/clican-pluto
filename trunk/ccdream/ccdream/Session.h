//
//  Session.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"

@interface Session : NSObject {
    int _sessionId;
    NSString* _title;
    NSString* _name;
    NSString* _type;
    NSString* _sponsor;
    NSDate* _startTime;
    NSDate* _lastUpdateTime;
    NSDate* _endTime;
    NSArray* _states;
    NSArray* _variables;
    NSString* _status;
    
}

@property (nonatomic,assign) int sessionId;
@property (nonatomic,retain) NSString* title;
@property (nonatomic,retain) NSString* name;
@property (nonatomic,retain) NSString* type;
@property (nonatomic,retain) NSString* sponsor;
@property (nonatomic,retain) NSDate* startTime;
@property (nonatomic,retain) NSDate* lastUpdateTime;
@property (nonatomic,retain) NSDate* endTime;
@property (nonatomic,retain) NSArray* states;
@property (nonatomic,retain) NSArray* variables;
@property (nonatomic,retain) NSString* status;

@end
