//
//  GlobalEventHandler.m
//  ccdream
//
//  Created by wei zhang on 12-6-5.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "GlobalEventHandler.h"


@implementation GlobalEventHandler

@synthesize delegateArray = _delegateArray;

static GlobalEventHandler *sharedHandler = nil;

+(GlobalEventHandler*) sharedHandler
{
	@synchronized(self) {
		if (sharedHandler == nil){
            sharedHandler = [[self alloc] init]; // assignment not done here
            sharedHandler.delegateArray = [CCArray array];
        }
	}
	return sharedHandler;
}

-(void) addPositionTouchDelegate:(id<PositionTouchDelegate>) delegate {
    [[self delegateArray] addObject:delegate];
}

-(void) removePositionTouchDelegate:(id<PositionTouchDelegate>) delegate{
    [[self delegateArray] removeObject:delegate];
}

- (BOOL)ccTouchBegan:(UITouch *)touch withEvent:(UIEvent *)event{
    CGPoint touchLocation = [touch locationInView: [touch view]];
    CCDirector *director = [CCDirector sharedDirector];
    CCLOG(@"touch location=%f,%f",touchLocation.x,[director winSize].height-touchLocation.y);
    int positionX = touchLocation.x/MAP_POINT_SIZE;
    int positionY = ([director winSize].height-touchLocation.y)/MAP_POINT_SIZE;
    Position* touchPosition = [Position positionWithX:positionX Y:positionY];
    CCLOG(@"touch position=%@",touchPosition.description);
    int count = [[self delegateArray] count];
    for(int i=0;i<count;i++){
        id<PositionTouchDelegate> delegate = [[self delegateArray] objectAtIndex:i];
        BOOL breakAndReturn = NO;
        @try{
            breakAndReturn = [delegate touchBegan:touchPosition withEvent:event];
        }@catch(NSException* ex){
            CCLOGERROR(@"Exception occured for touch %@, detail:%@",touchPosition.description,ex.description);
        }
        
        if(breakAndReturn){
            return NO;
        }
    }
    return NO;
}
@end
