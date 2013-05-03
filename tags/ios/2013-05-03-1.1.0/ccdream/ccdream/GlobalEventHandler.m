//
//  GlobalEventHandler.m
//  ccdream
//
//  Created by wei zhang on 12-6-5.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "GlobalEventHandler.h"
#import "MapLayer.h"

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
    [[self delegateArray] insertObject:delegate atIndex:0];
}

-(void) removePositionTouchDelegate:(id<PositionTouchDelegate>) delegate{
    [[self delegateArray] removeObject:delegate];
}

-(Position*) getPosition:(UITouch*) touch{
    CGPoint touchLocation = [touch locationInView: [touch view]];
    CCDirector *director = [CCDirector sharedDirector];
    CCLOG(@"touch location=%f,%f",touchLocation.x,[director winSize].height-touchLocation.y);
    int positionX = touchLocation.x/MAP_POINT_SIZE_X;
    int positionY = ([director winSize].height-touchLocation.y)/MAP_POINT_SIZE_Y;
    MapLayer* mapLayer = [MapLayer sharedMapLayer];
    Position* touchPosition = [Position positionWithX:positionX+mapLayer.xposiOffset Y:positionY+mapLayer.yposiOffset];
    CCLOG(@"touch position=%@",touchPosition.description);

    return touchPosition;
}

- (BOOL)ccTouchBegan:(UITouch *)touch withEvent:(UIEvent *)event{
        int count = [[self delegateArray] count];
    Position* touchPosition = [self getPosition:touch];
    for(int i=0;i<count;i++){
        id<PositionTouchDelegate> delegate = [[self delegateArray] objectAtIndex:i];
        BOOL breakAndReturn = NO;
        @try{
            breakAndReturn = [delegate touchBegan:touchPosition withEvent:event];
        }@catch(NSException* ex){
            CCLOGERROR(@"Exception occured for touch %@, detail:%@",touchPosition.description,ex.description);
        }
        
        if(breakAndReturn){
            return YES;
        }
    }
    return YES;
}

- (void)ccTouchMoved:(UITouch *)touch withEvent:(UIEvent *)event{
    Position* touchPosition = [self getPosition:touch];
    int count = [[self delegateArray] count];
    for(int i=0;i<count;i++){
        id<PositionTouchDelegate> delegate = [[self delegateArray] objectAtIndex:i];
        @try{
            [delegate touchMoved:touchPosition withEvent:event];
        }@catch(NSException* ex){
            CCLOGERROR(@"Exception occured for touch %@, detail:%@",touchPosition.description,ex.description);
        }
    }

}

- (void)ccTouchEnded:(UITouch *)touch withEvent:(UIEvent *)event
{
    Position* touchPosition = [self getPosition:touch];
    int count = [[self delegateArray] count];
    for(int i=0;i<count;i++){
        id<PositionTouchDelegate> delegate = [[self delegateArray] objectAtIndex:i];
        @try{
            [delegate touchEnded:touchPosition withEvent:event];
        }@catch(NSException* ex){
            CCLOGERROR(@"Exception occured for touch %@, detail:%@",touchPosition.description,ex.description);
        }
    }
}
- (void)ccTouchCancelled:(UITouch *)touch withEvent:(UIEvent *)event{
    Position* touchPosition = [self getPosition:touch];
    int count = [[self delegateArray] count];
    for(int i=0;i<count;i++){
        id<PositionTouchDelegate> delegate = [[self delegateArray] objectAtIndex:i];
        @try{
            [delegate touchCancelled:touchPosition withEvent:event];
        }@catch(NSException* ex){
            CCLOGERROR(@"Exception occured for touch %@, detail:%@",touchPosition.description,ex.description);
        }
    }
}

@end
