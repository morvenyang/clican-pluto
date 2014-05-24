//
//  PieChartView.m
//  PieChartViewDemo
//
//  Created by Strokin Alexey on 8/27/13.
//  Copyright (c) 2013 Strokin Alexey. All rights reserved.
//

#import "PieChartView.h"
#import <QuartzCore/QuartzCore.h>
#import "StyleSheet.h"
@implementation PieChartView

@synthesize delegate;
@synthesize datasource;

-(id)initWithFrame:(CGRect)frame
{
   self = [super initWithFrame:frame];
   if (self != nil)
   {
      //initialization
      self.backgroundColor = [UIColor clearColor];
      self.layer.shadowColor = [[UIColor blackColor] CGColor];
      self.layer.shadowOffset = CGSizeMake(0.0f, 2.5f);
      self.layer.shadowRadius = 1.9f;
      self.layer.shadowOpacity = 0.9f;
   
   }
   return self;
}

-(void)reloadData
{
   [self setNeedsDisplay];
}

- (void)drawRect:(CGRect)rect
{

//prepare
   CGContextRef context = UIGraphicsGetCurrentContext();
   CGFloat theHalf = rect.size.width/2;
   CGFloat lineWidth = theHalf;
   if ([self.delegate respondsToSelector:@selector(centerCircleRadius)])
   {
      lineWidth -= [self.delegate centerCircleRadius];
      NSAssert(lineWidth <= theHalf, @"wrong circle radius");
   }
   CGFloat radius = theHalf-lineWidth/2;
   
   CGFloat centerX = theHalf;
   CGFloat centerY = rect.size.height/2;
   
//drawing
   
   int sum = 0;
   int slicesCount = [self.datasource numberOfSlicesInPieChartView:self];
   
   for (int i = 0; i < slicesCount; i++)
   {
      sum += [self.datasource pieChartView:self valueForSliceAtIndex:i];
   }
   
   float startAngle = - M_PI_2;
   float endAngle = 0.0f;
      
   for (int i = 0; i < slicesCount; i++)
   {
      double value = [self.datasource pieChartView:self valueForSliceAtIndex:i];

      endAngle = startAngle + M_PI*2*value/sum;
      CGContextAddArc(context, centerX, centerY, radius, startAngle, endAngle, false);
   
      UIColor  *drawColor = [self.datasource pieChartView:self colorForSliceAtIndex:i];
   
      CGContextSetStrokeColorWithColor(context, drawColor.CGColor);
   	CGContextSetLineWidth(context, lineWidth);
   	CGContextStrokePath(context);
      startAngle += M_PI*2*value/sum;
   }
    
    UIFont* font1 = [UIFont systemFontOfSize:24];
    UIFont* font2 = [UIFont systemFontOfSize:48];
    UIFont* font3 = [UIFont systemFontOfSize:24];
    CGFloat fontHeight = font1.pointSize+font2.pointSize+font3.pointSize;
    CGFloat yOffset = (rect.size.height - fontHeight) / 2.0;
    CGContextSetFillColorWithColor(context, [StyleSheet colorFromHexString:@"#5f5f5f"].CGColor);
    NSString* s1 = @"总零售收入";
    CGRect textRect1 = CGRectMake(0, yOffset, rect.size.width, font1.pointSize);
    [s1 drawInRect: textRect1
         withFont: font1
    lineBreakMode: NSLineBreakByWordWrapping
        alignment: NSTextAlignmentCenter];
    yOffset=yOffset+font1.pointSize;
    
    CGContextSetFillColorWithColor(context, [StyleSheet colorFromHexString:@"#ff6501"].CGColor);
    NSString* s2 = [NSString stringWithFormat:@"%d",sum];
    CGRect textRect2 = CGRectMake(0, yOffset, rect.size.width, font2.pointSize);
    [s2 drawInRect: textRect2
          withFont: font2
     lineBreakMode: NSLineBreakByWordWrapping
         alignment: NSTextAlignmentCenter];
    
    yOffset=yOffset+font2.pointSize;
    CGContextSetFillColorWithColor(context, [StyleSheet colorFromHexString:@"#5f5f5f"].CGColor);
    NSString* s3 = @"万元";
    CGRect textRect3 = CGRectMake(0, yOffset, rect.size.width, font2.pointSize);
    [s3 drawInRect: textRect3
          withFont: font3
     lineBreakMode: NSLineBreakByWordWrapping
         alignment: NSTextAlignmentCenter];
}

@end
