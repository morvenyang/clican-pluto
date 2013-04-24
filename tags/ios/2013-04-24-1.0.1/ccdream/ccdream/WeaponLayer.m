//
//  WeaponLayer.m
//  ccdream
//
//  Created by wei zhang on 12-6-13.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "WeaponLayer.h"
#import "Weapon.h"
#import "WeaponMenuItem.h"
#import "MapLayer.h"
#import "WorkflowConstants.h"
#import "EventDispatcher.h"
#import "PositionUtil.h"
@implementation WeaponLayer

@synthesize weaponMenu = _weaponMenu;
@synthesize weapons = _weapons;
@synthesize targetCharacterArray = _targetCharacterArray;
@synthesize targetShadowSpriteArray = _targetShadowSpriteArray;

static WeaponLayer* _sharedWeaponLayer = nil;

-(id) init{
    self = [super init];
    if(self){
        self.weapons = [CCArray array];
        self.targetShadowSpriteArray = [CCArray array];
        self.targetCharacterArray = [CCArray array];
    }
    return self;
}
+(WeaponLayer*) sharedWeaponLayer{
    @synchronized(self) {
        if (_sharedWeaponLayer == nil){
            _sharedWeaponLayer = [WeaponLayer node];
        }
    }
    return _sharedWeaponLayer;
}

-(void) showAtPosition:(Position*) position charPosi:(Position*) charPosi character:(Character*) character{
    [self.weapons removeAllObjects];
    MapLayer* mapLayer = [MapLayer sharedMapLayer];
    if(self.weaponMenu!=nil){
        [self.weaponMenu removeFromParentAndCleanup:YES];
    }
    int i = 0;
    WeaponMenuItem* item0 = nil;
    WeaponMenuItem* item1 = nil;
    WeaponMenuItem* item2 = nil;
    WeaponMenuItem* item3 = nil;
    for (Weapon* weapon in character.weapons) {
        if([PositionUtil canAttack:charPosi targetCharacterArray:mapLayer.enemyCharacterArray rangeSet:weapon.rangeSet]){
            [self.weapons addObject:weapon];
            if(i==0){
                item0 = [WeaponMenuItem itemWithLabel:[CCLabelTTF labelWithString:weapon.name fontName:@"Marker Felt" fontSize:20] normalImage:weapon.imageFile target:self selector:@selector(selectWeapon0:)];
                
            }else if(i==1){
                item1 = [WeaponMenuItem itemWithLabel:[CCLabelTTF labelWithString:weapon.name fontName:@"Marker Felt" fontSize:20] normalImage:weapon.imageFile target:self selector:@selector(selectWeapon1:)];
                
            }
            else if(i==2){
                item2 = [WeaponMenuItem itemWithLabel:[CCLabelTTF labelWithString:weapon.name fontName:@"Marker Felt" fontSize:20] normalImage:weapon.imageFile target:self selector:@selector(selectWeapon2:)];
                
            }else if(i==3){
                item3 = [WeaponMenuItem itemWithLabel:[CCLabelTTF labelWithString:weapon.name fontName:@"Marker Felt" fontSize:20] normalImage:weapon.imageFile target:self selector:@selector(selectWeapon3:)];
                
            }
            i++;
        }
    }
    if(item0==nil){
        self.weaponMenu = [CCMenu menuWithItems: nil];
    }else if(item1==nil){
        self.weaponMenu = [CCMenu menuWithItems:item0, nil];
    }else if(item2==nil){
        self.weaponMenu = [CCMenu menuWithItems:item0,item1, nil];
    }else if(item3==nil){
        self.weaponMenu = [CCMenu menuWithItems:item0,item2,item2, nil];
    }else{
        self.weaponMenu = [CCMenu menuWithItems:item0,item1,item2,item3,nil];
    }
    CCLOG(@"weapon menu position:%@",position.description);
    self.weaponMenu.position = [position toCenterCGPoint];
    [self.weaponMenu alignItemsVertically];
    [self addChild:self.weaponMenu];
    [[GlobalEventHandler sharedHandler] addPositionTouchDelegate:self];
    self.visible = YES;
}

-(void) selectWeapon:(int) index{
    CCLOG(@"selectWeapon%i",index);
    [self hide];
    NSMutableDictionary* param = [[[NSMutableDictionary alloc] init] autorelease];
    Weapon* selectedWeapon = [self.weapons objectAtIndex:index];
    CCLOG(@"selectedWeapon=%@",selectedWeapon.name);
    [param setValue:selectedWeapon forKey:PARAM_SELECTED_WEAPON];
    [[EventDispatcher sharedEventDispatcher] dispatch:[MapLayer sharedMapLayer].fightMapSession.sessionId forState:0 forEventType:EVENT_TYPE_WL_SELECT_WEAPON forParameters:param];
}
-(void) selectWeapon0:(id) sender{
    [self selectWeapon:0];
}

-(void) selectWeapon1:(id) sender{
    [self selectWeapon:1];
}

-(void) selectWeapon2:(id) sender{
    [self selectWeapon:2];
}

-(void) selectWeapon3:(id) sender{
    [self selectWeapon:3];
}

-(void) hide{
    [[GlobalEventHandler sharedHandler] removePositionTouchDelegate:self];
    self.visible = NO;
}

- (BOOL)touchBegan:(Position *)posi withEvent:(UIEvent *)event {
    self.visible = NO;
    return NO;
}

- (void)dealloc {
    [_weaponMenu release];
    _weaponMenu = nil;
    [_targetCharacterArray release];
    _targetCharacterArray = nil;
    [_targetShadowSpriteArray release];
    _targetShadowSpriteArray = nil;
    [super dealloc];
}

@end
