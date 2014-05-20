//
//  Brand.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "Brand.h"

@implementation Brand

@synthesize brand = _brand;
@synthesize date = _date;
@synthesize dayAmount = _dayAmount;
@synthesize weekAmount = _weekAmount;
@synthesize yearAmount = _yearAmount;
@synthesize weekLike = _weekLike;
@synthesize yearLike = _yearLike;

- (void) dealloc {
    TT_RELEASE_SAFELY(_brand);
    TT_RELEASE_SAFELY(_date);
    TT_RELEASE_SAFELY(_dayAmount);
    TT_RELEASE_SAFELY(_weekAmount);
    TT_RELEASE_SAFELY(_yearAmount);
    TT_RELEASE_SAFELY(_weekLike);
    TT_RELEASE_SAFELY(_yearLike);
    [super dealloc];
}

-(int) getIndex{
    if([self.brand isEqualToString:@"女装"]){
        return 1;
    }else if([self.brand isEqualToString:@"男装"]){
        return 2;
    }else if([self.brand isEqualToString:@"乐町"]){
        return 3;
    }else if([self.brand isEqualToString:@"赫奇"]){
        return 4;
    }else if([self.brand isEqualToString:@"MG"]){
        return 5;
    }else if([self.brand isEqualToString:@"童装"]){
        return 6;
    }else{
        return 100;
    }
}
-(NSComparisonResult) compare:(Brand*) otherObject{
    if([self getIndex]<[otherObject getIndex]){
        return NSOrderedAscending;
    }else if([self getIndex]>[otherObject getIndex]){
        return NSOrderedDescending;
    }else{
        return NSOrderedSame;
    }
}
@end
