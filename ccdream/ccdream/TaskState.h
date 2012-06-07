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
}

@property (nonatomic,retain) NSMutableArray* taskListeners;

@end
