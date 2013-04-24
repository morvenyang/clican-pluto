//
//  Constants.m
//  ccdream
//
//  Created by wei zhang on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Constants.h"


@implementation Constants

static NSDictionary* _weapons = nil;

+(Weapon*) getWeapon:(NSString*) code{
    @synchronized(self) {
        if (_weapons == nil){
            NSBundle *bundle = [NSBundle mainBundle]; //取得mainBundle
            NSString *plistPath = [bundle pathForResource:@"weapon" ofType:@"plist"]; //取得文件路径
            //读取到一个NSDictionary
            _weapons = [[NSDictionary alloc] initWithContentsOfFile:plistPath];
        }
    }
    Weapon* weapon = [Weapon weaponWithDict:[_weapons objectForKey:code]];
    return weapon;
}
@end
