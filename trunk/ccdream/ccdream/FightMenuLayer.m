//
//  FightMenuLayer.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "FightMenuLayer.h"


@implementation FightMenuLayer

@synthesize fightMenu = _fightMenu;
@synthesize attack = _attack;
@synthesize cancel = _cancel;
@synthesize standby = _standby;

static FightMenuLayer* sharedFightMenuLayer = nil;

-(id) init{
    self = [super init];
    if(self!=nil){
        [[GlobalEventHandler sharedHandler] addPositionTouchDelegate:self];
    }
    return self;
}



+(FightMenuLayer*) sharedFightMenuLayer
{
	@synchronized(self) {
		if (sharedFightMenuLayer == nil){
            sharedFightMenuLayer = [FightMenuLayer node]; // assignment not done here
            
            
            sharedFightMenuLayer.attack = [CCMenuItemFont 
                                           itemFromString:@"攻击" target:sharedFightMenuLayer selector:@selector(attack:)];
            sharedFightMenuLayer.standby = [CCMenuItemFont 
                                           itemFromString:@"待机" target:sharedFightMenuLayer selector:@selector(standby:)];
            sharedFightMenuLayer.cancel = [CCMenuItemFont 
                                           itemFromString:@"取消" target:sharedFightMenuLayer selector:@selector(cancel:)];
            sharedFightMenuLayer.fightMenu = [CCMenu menuWithItems:sharedFightMenuLayer.attack,sharedFightMenuLayer.standby,sharedFightMenuLayer.cancel,nil];
            [sharedFightMenuLayer.fightMenu alignItemsVertically];
            sharedFightMenuLayer.fightMenu.position = ccp(100, 100);
            [sharedFightMenuLayer addChild:sharedFightMenuLayer.fightMenu];
        }
	}
	return sharedFightMenuLayer;
}

-(void) showAtPosition:(Position*) position{
    CCLOG(@"position%@",position);
    self.fightMenu.position = [position toCenterCGPoint];
    self.visible = YES;
}

-(void) hide{
    self.visible = NO;
}

-(void) attack:(id) sender {
    CCLOG(@"attack");
}

-(void) standby:(id) sender {
    CCLOG(@"standby");
}

-(void) cancel:(id) sender {
    CCLOG(@"cancel");
}

- (BOOL)touchBegan:(Position *)posi withEvent:(UIEvent *)event {
    return YES;
}

- (void)selectCharacter:(Character*) character{
    
}

- (void)dealloc {
    [_fightMenu release];
    _fightMenu = nil;
    [_attack release];
    _attack = nil;
    [_standby release];
    _standby = nil;
    [_cancel release];
    _cancel = nil;
    [super dealloc];
}


@end
