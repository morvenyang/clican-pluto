//
//  Weapon.m
//  ccdream
//
//  Created by wei zhang on 12-6-10.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Weapon.h"


@implementation Weapon

@synthesize name= _name;
@synthesize image = _image;
@synthesize imageFile = _imageFile;
@synthesize weaponSprite = _weaponSprite;
@synthesize minRange = _minRange;
@synthesize maxRange = _maxRange;
@synthesize rangeSet = _rangeSet;
@synthesize phyPower = _phyPower;
@synthesize magPower = _magPower;
@synthesize requiredLevel = _requiredLevel;
@synthesize weight = _weight;
@synthesize hit = _hit;
@synthesize critical = _critical;
@synthesize armorAgainst = _armorAgainst;
@synthesize cavalryAgainst = _cavalryAgainst;
@synthesize flyAgainst = _flyAgainst;
@synthesize monsterAgainst = _monsterAgainst;

-(id) init{
    self = [super init];
    if(self){
        self.rangeSet = [[[NSMutableSet alloc] init] autorelease];
    }
    return self;
}

+(Weapon*) weaponWithDict:(NSDictionary*) dict{
    Weapon* weapon = [[[Weapon alloc] init] autorelease];
    weapon.name = [dict objectForKey:@"name"];
    weapon.imageFile = [dict objectForKey:@"image"];
    weapon.image = [UIImage imageNamed:[dict objectForKey:@"image"]];
    CCTexture2D *texture = [[[CCTexture2D alloc] initWithImage:weapon.image] autorelease];
    weapon.weaponSprite = [CCSprite spriteWithTexture:texture];
    weapon.minRange = [[dict objectForKey:@"minRange"] intValue];
    weapon.maxRange = [[dict objectForKey:@"maxRange"] intValue];
    for(int i =weapon.minRange;i<=weapon.maxRange;i++){
        [weapon.rangeSet addObject:[NSNumber numberWithInt:i]];
    }
    weapon.phyPower = [[dict objectForKey:@"phyPower"] intValue];
    weapon.magPower = [[dict objectForKey:@"magPower"] intValue];
    weapon.requiredLevel = [[dict objectForKey:@"requiredLevel"] intValue];
    weapon.weight = [[dict objectForKey:@"weight"] intValue];
    weapon.hit = [[dict objectForKey:@"hit"] intValue];
    weapon.critical = [[dict objectForKey:@"critical"] intValue];
    weapon.armorAgainst = [[dict objectForKey:@"armorAgainst"] boolValue];
    weapon.cavalryAgainst = [[dict objectForKey:@"cavalryAgainst"] boolValue];
    weapon.flyAgainst = [[dict objectForKey:@"flyAgainst"] boolValue];
    weapon.monsterAgainst = [[dict objectForKey:@"monsterAgainst"] boolValue];
    return weapon;
}

-(NSString*)description{
    return [@"" stringByAppendingFormat:@"name:%@,minRange:%i,maxRange:%i",_name,_minRange,_maxRange];
}

- (void)dealloc {
    [_name release];
    _name = nil;
	[_image release];
    _image = nil;
    [_imageFile release];
    _imageFile = nil;
    [_weaponSprite release];
    _weaponSprite = nil;
    [_rangeSet release];
    _rangeSet = nil;
    [super dealloc];
}
@end
