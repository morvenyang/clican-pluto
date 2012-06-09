//
//  TaskState.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "IState.h"

@interface TaskState : IState {
    NSMutableArray* _taskListeners;
    NSString* _assignees;
    NSString* _taskName;
    NSString* _taskType;
}

@property (nonatomic,retain) NSMutableArray* taskListeners;
@property (nonatomic,retain) NSString* assignees;
@property (nonatomic,retain) NSString* taskName;
@property (nonatomic,retain) NSString* taskType;
@end
