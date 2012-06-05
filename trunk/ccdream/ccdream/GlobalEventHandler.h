//
//  GlobalEventHandler.h
//  ccdream
//
//  Created by wei zhang on 12-6-5.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Position.h"

@protocol PositionTouchDelegate;


@interface GlobalEventHandler : NSObject<CCTargetedTouchDelegate> {
    CCArray* _delegateArray;
}

@property (nonatomic,retain) CCArray* delegateArray;

+(GlobalEventHandler*) sharedHandler;

-(void) addPositionTouchDelegate:(id<PositionTouchDelegate>) delegate;

-(void) removePositionTouchDelegate:(id<PositionTouchDelegate>) delegate;

@end


@protocol PositionTouchDelegate <NSObject>

- (BOOL)touchBegan:(Position *)posi withEvent:(UIEvent *)event;

@optional
// touch updates:
- (void)touchMoved:(Position *)posi withEvent:(UIEvent *)event;
- (void)touchEnded:(Position *)posi withEvent:(UIEvent *)event;
- (void)touchCancelled:(UITouch *)touch withEvent:(UIEvent *)event;

@end