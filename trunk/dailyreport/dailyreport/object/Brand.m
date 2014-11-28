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

- (void) dealloc {
    TT_RELEASE_SAFELY(_brand);
    TT_RELEASE_SAFELY(_date);
    TT_RELEASE_SAFELY(_dayAmount);
    [super dealloc];
}

-(int) getIndex{
    if([self.brand isEqualToString:@"女装"]){
        return 1;
    }else if([self.brand isEqualToString:@"男装"]){
        return 2;
    }else if([self.brand isEqualToString:@"乐町"]){
        return 3;
    }else if([self.brand isEqualToString:@"童装"]){
        return 4;
    }else if([self.brand isEqualToString:@"赫奇"]){
        return 5;
    }else if([self.brand isEqualToString:@"MG公司"]){
        return 6;
    }else if([self.brand isEqualToString:@"电商"]){
        return 7;
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
